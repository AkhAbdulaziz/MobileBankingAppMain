package uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.UserLocalData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.local.LocalStorage
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api.AuthApi
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api.ProfileApi
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.request.UserInfoRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.AvatarResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.setApiClientOpenLoginScreenListener
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.*
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val profileApi: ProfileApi,
    private val localStorage: LocalStorage
) : AuthRepository {
    private val gson = Gson()

    private var userDataSavedListener: (() -> Unit)? = null
    override fun setUserDataSavedListener(f: () -> Unit) {
        userDataSavedListener = f
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
        var message = "Xatolik yuzaga keldi"
        val response = authApi.register(data)
        if (response.isSuccessful) {
            message = response.body()!!.message
            emit(Result.success(response.body()!!.message))
            localStorage.userPassword = data.password
            localStorage.userPhone = data.phone
            localStorage.userFullName = "${data.firstName} ${data.lastName}"
        } else {
            response.errorBody()?.let {
                message = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure(Throwable(message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun loginUser(data: LoginRequest): Flow<Result<LoginResponse>> = flow {
        val response = authApi.login(data)
        var message = response.errorBody().toString()

        if (response.isSuccessful && response.body() != null) {
            localStorage.userPassword = data.password
            localStorage.userPhone = data.phone
            localStorage.refreshToken = response.body()!!.data?.refresh_token!!
            localStorage.accessToken = response.body()!!.data?.access_token!!
            localStorage.startScreen = StartScreenEnum.MAIN
            emit(Result.success(response.body()!!))
        } else {
            response.errorBody()?.let {
                message = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure(Throwable(message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun loginUser(): Flow<Result<LoginResponse>> = flow {
        val data = LoginRequest(localStorage.userPhone, localStorage.userPassword)
        val response = authApi.login(data)
        var message = response.errorBody().toString()

        if (response.isSuccessful && response.body() != null) {
            localStorage.userPassword = data.password
            localStorage.userPhone = data.phone
            localStorage.refreshToken = response.body()!!.data?.refresh_token!!
            localStorage.accessToken = response.body()!!.data?.access_token!!
            localStorage.startScreen = StartScreenEnum.MAIN
            emit(Result.success(response.body()!!))
        } else {
            response.errorBody()?.let {
                message = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure(Throwable(message)))
        }
    }.flowOn(Dispatchers.IO)

    /*
     override fun loginUser(data: LoginRequest): Flow<Result<LoginResponse>> = flow {
        val response = authApi.login(data)
        var message = response.message()
        if (response.isSuccessful && (response != App.instance.getString(R.string.error_login_password_incorrect)) && (response.message() != App.instance.getString(
                R.string.error_login_phone_number_not_available
            ))
        ) {
            localStorage.userPassword = data.password
            localStorage.userPhone = data.phone
            localStorage.refreshToken = response.body()!!.data?.refresh_token!!
            localStorage.accessToken = response.body()!!.data?.access_token!!
            localStorage.startScreen = StartScreenEnum.MAIN
            emit(Result.success(response.body()!!))
        } else {
            response.errorBody()?.let {
                message = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure(Throwable(message)))
        }
    }.flowOn(Dispatchers.IO)
    */


    override fun verifyUser(data: VerifyUserRequest): Flow<Result<VerifyUserResponse>> = flow {
        val response = authApi.verifyUser(data)
        if (response.isSuccessful) {
            response.body()?.let {
                localStorage.refreshToken = it.data?.refresh_token!!
                localStorage.accessToken = it.data.access_token!!
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

    override fun logoutUser(): Flow<Result<LogoutResponse>> = flow {
        val response = authApi.logout(localStorage.accessToken)
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

    override fun getUserPhoneNumber(): Flow<Result<String>> = flow {
        emit(Result.success(localStorage.userPhone))
    }.flowOn(Dispatchers.IO)

    override fun setUserToken(token: String) {
        localStorage.accessToken = token
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
        val requestData = UserInfoRequest(data.firstName, data.lastName, localStorage.userPassword)
        val response = profileApi.putUserInfo(requestData)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getProfileInfo(): Flow<Result<ProfileInfoResponse>> = flow {
        val response = profileApi.getProfileInfo()
        if (response.isSuccessful) {
            response.body()!!.data!!.username = localStorage.userNickName
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserFullName(): String {
        return localStorage.userFullName
    }

    override fun getUserPassword(): String {
        return localStorage.userPassword
    }

    override fun getUserLocalData(): Flow<Result<UserLocalData>> = flow {
        emit(
            Result.success(
                UserLocalData(
                    localStorage.userFirstName,
                    localStorage.userLastName,
                    localStorage.userNickName,
                    localStorage.userPhoneNumber1,
                    localStorage.userPhoneNumber2,
                    localStorage.userGender,
                    localStorage.userBirthday
                )
            )
        )
    }.flowOn(Dispatchers.IO)

    override fun saveUserData(userLocalData: UserLocalData) {
        localStorage.userFirstName = userLocalData.firstName
        localStorage.userLastName = userLocalData.lastName
        localStorage.userNickName = userLocalData.nickname
        localStorage.userPhoneNumber1 = userLocalData.phoneNumber1
        localStorage.userPhoneNumber2 = userLocalData.phoneNumber2
        localStorage.userGender = userLocalData.gender
        localStorage.userBirthday = userLocalData.birthday
        userDataSavedListener?.invoke()
    }

    override fun changeFaceIDPermission(permission: Boolean) {
        localStorage.permissionFaceID = permission
    }

    override fun changeConfirmationByFingerPrintPermission(permission: Boolean) {
        localStorage.permissionConfirmPaymentByFingerPrint = permission
    }

    override fun getFaceIDPermission(): Flow<Result<Boolean>> = flow {
        emit(Result.success(localStorage.permissionFaceID))
    }.flowOn(Dispatchers.IO)

    override fun getConfirmationByFingerPrintPermission(): Boolean {
        return localStorage.permissionConfirmPaymentByFingerPrint
    }

    override fun getPinCode(): Flow<Result<String>> = flow {
        emit(Result.success(localStorage.pinCode))

    }.flowOn(Dispatchers.IO)

    override fun savePinCode(pinCode: String) {
        localStorage.pinCode = pinCode
    }
}
