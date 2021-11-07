package uz.gita.mobilebankingapp.domain.repository.impl

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import uz.gita.mobilebankingapp.data.ApiClient
import uz.gita.mobilebankingapp.data.api.ProfileApi
import uz.gita.mobilebankingapp.data.api.UserApi
import uz.gita.mobilebankingapp.data.model.profile_req_res.request.UserInfoRequest
import uz.gita.mobilebankingapp.data.model.profile_req_res.response.AvatarResponse
import uz.gita.mobilebankingapp.data.model.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.model.user_req_res.request.*
import uz.gita.mobilebankingapp.data.model.user_req_res.response.*
import uz.gita.mobilebankingapp.data.pref.MySharedPreferences
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val userApi = ApiClient.retrofit.create(UserApi::class.java)
    private val profileApi = ApiClient.retrofit.create(ProfileApi::class.java)
    private val gson = Gson()
    private val pref = MySharedPreferences.getPref()

    override fun registerUser(data: RegisterRequest): Flow<Result<String>> = flow {
        val response = userApi.register(data)
        Log.d("TTT", "register response = $response")
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.message))
            pref.userPhone = data.phone
        } else {
            var st = "Serverga ulanishda xatolik bo'ldi"
            ResponseBody
            emit(Result.failure<String>(Throwable(st)))
        }

    }.flowOn(Dispatchers.IO)

    override fun loginUser(data: LoginRequest): Flow<Result<String>> = flow {
        val response = userApi.login(data)
        Log.d("TTT", "login response = $response")
        if (response.isSuccessful) {
            pref.userPhone = data.phone
            emit(Result.success(response.body()!!.message))
        } else {
            var st = "Serverga ulanishda xatolik bo'ldi"
            ResponseBody
            emit(Result.failure<String>(Throwable(st)))
        }

    }.flowOn(Dispatchers.IO)

    override fun logoutUser(): Flow<Result<LogoutResponse>> = flow {
        val response = userApi.logout()
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun resend(data: ResendRequest): Flow<Result<BaseResponse>> = flow {
        val response = userApi.resend(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun reset(data: ResetUserRequest): Flow<Result<BaseResponse>> = flow {
        val response = userApi.reset(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun setNewPassword(data: NewPasswordRequest): Flow<Result<BaseResponse>> = flow {
        val response = userApi.setNewPassword(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun refresh(data: RefreshRequest): Flow<Result<RefreshResponse>> = flow {
        val response = userApi.refresh(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun verifyUser(request: VerifyUserRequest): Flow<Result<Unit>> = flow {
        val response = userApi.verifyUser(request)
        if (response.isSuccessful) {
            response.body()?.let {
                pref.refreshToken = it.data.refreshToken
                pref.accessToken = it.data.accessToken
            }
            emit(Result.success(Unit))
        } else {
            emit(Result.failure<Unit>(Throwable(response.errorBody().toString())))
        }

    }

    override fun getUserPhoneNumber(): String {
        return pref.userPhone
    }

    override fun setUserToken(token: String) {
        pref.accessToken = token
    }

    override fun putUserAvatar(data: File): Flow<Result<BaseResponse>> = flow {
        val response = profileApi.putUserAvatar(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserAvatar(): Flow<Result<AvatarResponse>> = flow {
        val response = profileApi.getUserAvatar()
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun putUserInfo(data: UserInfoRequest): Flow<Result<BaseResponse>> = flow {
        val response = profileApi.putUserInfo(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getProfileInfo(): Flow<Result<ProfileInfoResponse>> = flow {
        val response = profileApi.getProfileInfo()
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

}