package uz.gita.mobilebankingapp.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.local.MySharedPreferences
import uz.gita.mobilebankingapp.data.remote.api.AuthApi
import uz.gita.mobilebankingapp.data.remote.api.ProfileApi
import uz.gita.mobilebankingapp.data.remote.profile_req_res.request.UserInfoRequest
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.AvatarResponse
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.remote.setApiClientOpenLoginScreenListener
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.*
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.BaseResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.RefreshResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.VerifyUserResponse
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val profileApi: ProfileApi,
    private val pref: MySharedPreferences
) : AuthRepository {
    private val gson = Gson()

    private var userLocalDataListener: (() -> Unit)? = null
    override fun setUserLocalDataListener(f: () -> Unit) {
        userLocalDataListener = f
    }

    private var openLoginScreenListener: (() -> Unit)? = null
    override fun setOpenLoginScreenListener(f: () -> Unit) {
        openLoginScreenListener = f
    }

    init {
        setApiClientOpenLoginScreenListener {
            openLoginScreenListener?.invoke()
        }
    }

    override fun registerUser(data: RegisterRequest): Flow<Result<String>> = flow {
        val response = authApi.register(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.message))
            pref.userPassword = data.password
            pref.userPhone = data.phone
            pref.userFullName = "${data.firstName} ${data.lastName}"
        } else {
            var message = "Xatolik yuzaga keldi"
            response.errorBody()?.let {
                message = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure<String>(Throwable(message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun loginUser(data: LoginRequest): Flow<Result<String>> = flow {
        val response = authApi.login(data)
        if (response.isSuccessful) {
            pref.userPassword = data.password
            pref.userPhone = data.phone
            emit(Result.success(response.body()!!.message))
        } else {
            var message = "Xatolik yuzaga keldi"
            response.errorBody()?.let {
                message = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure<String>(Throwable(message)))
        }

    }.flowOn(Dispatchers.IO)

    override fun logoutUser(): Flow<Result<LogoutResponse>> = flow {
        val response = authApi.logout(pref.accessToken)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun resend(data: ResendRequest): Flow<Result<BaseResponse>> = flow {
        val response = authApi.resend(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun reset(data: ResetUserRequest): Flow<Result<BaseResponse>> = flow {
        val response = authApi.reset(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun setNewPassword(data: NewPasswordRequest): Flow<Result<BaseResponse>> = flow {
        val response = authApi.setNewPassword(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun refresh(data: RefreshRequest): Flow<Result<RefreshResponse>> = flow {
        val response = authApi.refresh(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun verifyUser(data: VerifyUserRequest): Flow<Result<VerifyUserResponse>> = flow {
        val response = authApi.verifyUser(data)
        if (response.isSuccessful) {
            response.body()?.let {
                pref.refreshToken = it.data?.refresh_token!!
                pref.accessToken = it.data.access_token!!
            }
            emit(Result.success(response.body()!!))
        } else {
            var message = "Xatolik yuzaga keldi"
            response.errorBody()?.let {
                message = gson.fromJson(it.toString(), BaseResponse::class.java).message
            }
            emit(Result.failure(Throwable(message)))
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

    override fun getUserFullName(): String {
        return pref.userFullName
    }

    override fun getUserPassword(): String {
        return pref.userPassword
    }

    override fun getUserLocalData(): UserLocalData {
        return UserLocalData(
            pref.userFirstName,
            pref.userLastName,
            pref.userNickName,
            pref.userPhoneNumber1,
            pref.userPhoneNumber2,
            pref.userGender,
            pref.userBirthday
        )
    }

    override fun setUserLocalData(userLocalData: UserLocalData) {
        pref.userFirstName = userLocalData.firstName
        pref.userLastName = userLocalData.lastName
        pref.userNickName = userLocalData.nickname
        pref.userPhoneNumber1 = userLocalData.phoneNumber1
        pref.userPhoneNumber2 = userLocalData.phoneNumber2
        pref.userGender = userLocalData.gender
        pref.userBirthday = userLocalData.birthday
        userLocalDataListener?.invoke()
    }
}