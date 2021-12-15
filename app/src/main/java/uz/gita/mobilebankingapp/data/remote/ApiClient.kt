package uz.gita.mobilebankingapp.data.remote

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.mobilebankingapp.BuildConfig.BASE_URL
import uz.gita.mobilebankingapp.BuildConfig.LOGGING
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.data.local.MySharedPreferences
import uz.gita.mobilebankingapp.utils.timber

object ApiClient {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(getHttpClient())
        .build()

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addLogging()
            .addInterceptor(refreshInterceptor())
            .addInterceptor(tokenInterceptor())
            .build()
    }
}

fun OkHttpClient.Builder.addLogging(): OkHttpClient.Builder {
    HttpLoggingInterceptor.Level.HEADERS
    val logging = object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            timber(message, "HTTP")
        }
    }
    if (LOGGING) {
        addInterceptor(ChuckInterceptor(App.instance))
        addInterceptor(HttpLoggingInterceptor(logging))
    }
    return this
}

fun tokenInterceptor() = Interceptor { chain ->
    val pref = MySharedPreferences()

    val chainRequest = chain.request()
    val newRequest =
        chainRequest.newBuilder().removeHeader("token").addHeader("token", pref.accessToken).build()

    val response = chain.proceed(newRequest)
    response
}

fun refreshInterceptor() = Interceptor { chain ->
    val request = chain.request()
    val response = chain.proceed(request)
    if (response.code == 401) {
        response.close()
        val pref = MySharedPreferences()
        val data = JSONObject()
        data.put("phone", pref.userPhone)
        val body =
            data.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val requestRefresh = request.newBuilder()
            .addHeader("refresh_token", pref.refreshToken)
            .method("POST", body)
            .url("${BASE_URL}auth/refresh")
            .build()

        val responseRefresh = chain.proceed(requestRefresh)
        if (responseRefresh.code == 401) {
            return@Interceptor responseRefresh// refresh token ham eskirdi login screen navigate
        }

        if (responseRefresh.code == 200) {
            responseRefresh.body?.let {
                val data = Gson().fromJson(it.string(), VerifyResponse::class.java)
                pref.accessToken = data.data.accessToken
                pref.refreshToken = data.data.refreshToken
            }

            responseRefresh.close()
            val requestTwo = request.newBuilder()
                .removeHeader("token")
                .addHeader("token", pref.accessToken)
                .build()
            return@Interceptor chain.proceed(requestTwo)
        }
    }

    return@Interceptor response
}
