package uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.UserLocalData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.request.UserInfoRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.AvatarResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.*
import java.io.File

interface AuthRepository {
    fun registerUser(data: RegisterRequest): Flow<Result<String>>

    fun verifyUser(data: VerifyUserRequest): Flow<Result<VerifyUserResponse>>

    fun loginUser(data: LoginRequest): Flow<Result<LoginResponse>>

    fun loginUser(): Flow<Result<LoginResponse>>

    fun logoutUser(): Flow<Result<LogoutResponse>>

    fun resend(data: ResendRequest): Flow<Result<BaseResponse>>

    fun reset(data: ResetUserRequest): Flow<Result<BaseResponse>>

    fun setNewPassword(data: NewPasswordRequest): Flow<Result<BaseResponse>>

    fun refresh(data: RefreshRequest): Flow<Result<RefreshResponse>>

    fun getUserPhoneNumber(): Flow<Result<String>>

    fun setUserToken(token: String)

    fun putUserAvatar(data: File): Flow<Result<BaseResponse>>

    fun getUserAvatar(): Flow<Result<AvatarResponse>>

    fun putUserInfo(data: UserInfoRequest): Flow<Result<BaseResponse>>

    fun getProfileInfo(): Flow<Result<ProfileInfoResponse>>

    fun getUserFullName(): String

    fun getUserPassword(): String

    fun getUserLocalData(): Flow<Result<UserLocalData>>

    fun saveUserData(userLocalData: UserLocalData)

    fun setUserDataSavedListener(f: () -> Unit)

    fun setOpenLoginScreenListener(f: () -> Unit)

    fun changeFaceIDPermission(permission: Boolean)

    fun changeConfirmationByFingerPrintPermission(permission: Boolean)

    fun getFaceIDPermission(): Flow<Result<Boolean>>

    fun getConfirmationByFingerPrintPermission(): Boolean

    fun getPinCode(): Flow<Result<String>>
    fun savePinCode(pinCode: String)
}