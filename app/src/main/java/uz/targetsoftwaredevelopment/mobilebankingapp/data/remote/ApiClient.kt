package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.targetsoftwaredevelopment.mobilebankingapp.BuildConfig.LOGGING
import uz.targetsoftwaredevelopment.mobilebankingapp.app.App
import uz.targetsoftwaredevelopment.mobilebankingapp.data.local.LocalStorage
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.VerifyUserResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.timber

private var openLoginScreenListener: (() -> Unit)? = null
fun setApiClientOpenLoginScreenListener(block: () -> Unit) {
    openLoginScreenListener = block
}

object ApiClient {
    private val localStorage = LocalStorage()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(localStorage.BASE_URL)
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
    val logging = HttpLoggingInterceptor.Logger { message -> timber(message, "HTTP") }
    if (LOGGING) {
        addInterceptor(
            ChuckerInterceptor.Builder(App.instance)
                .collector(ChuckerCollector(App.instance))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()
        )
        addInterceptor(HttpLoggingInterceptor(logging))
    }
    return this
}

fun tokenInterceptor() = Interceptor { chain ->
    val localStorage = LocalStorage()

    val chainRequest = chain.request()
    val newRequest =
        chainRequest.newBuilder().removeHeader("token").addHeader("token", localStorage.accessToken)
            .build()

    val response = chain.proceed(newRequest)
    response
}

fun refreshInterceptor() = Interceptor { chain ->
    val localStorage = LocalStorage()

    val request = chain.request()
    val response = chain.proceed(request)
    if (response.code == 401) {
        response.close()
        val data = JSONObject()
        data.put("phone", localStorage.userPhone)
        val body =
            data.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val requestRefresh = request.newBuilder()
            .addHeader("refresh_token", localStorage.refreshToken)
            .method("POST", body)
            .url("${localStorage.BASE_URL}auth/refresh")
            .build()

        val responseRefresh = chain.proceed(requestRefresh)
        if (responseRefresh.code == 401) {
            openLoginScreenListener?.invoke()
            // refresh token ham eskirdi login screen navigate
            return@Interceptor responseRefresh
        }

        if (responseRefresh.code == 200) {
            responseRefresh.body?.let {
                val data = Gson().fromJson(it.string(), VerifyUserResponse::class.java)
                localStorage.accessToken = data.data?.access_token!!
                localStorage.refreshToken = data.data.refresh_token!!
            }

            responseRefresh.close()
            val requestTwo = request.newBuilder()
                .removeHeader("token")
                .addHeader("token", localStorage.accessToken)
                .build()
            return@Interceptor chain.proceed(requestTwo)
        }
    }
    return@Interceptor response
}
