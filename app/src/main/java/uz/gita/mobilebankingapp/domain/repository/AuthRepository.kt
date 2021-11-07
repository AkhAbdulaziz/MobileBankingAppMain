package uz.gita.mobilebankingapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.mobilebankingapp.data.model.profile_req_res.request.UserInfoRequest
import uz.gita.mobilebankingapp.data.model.profile_req_res.response.AvatarResponse
import uz.gita.mobilebankingapp.data.model.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.model.user_req_res.request.*
import uz.gita.mobilebankingapp.data.model.user_req_res.response.BaseResponse
import uz.gita.mobilebankingapp.data.model.user_req_res.response.LogoutResponse
import uz.gita.mobilebankingapp.data.model.user_req_res.response.RefreshResponse
import java.io.File

interface AuthRepository {
    fun registerUser(data: RegisterRequest): Flow<Result<String>>

    fun verifyUser(data: VerifyUserRequest): Flow<Result<Unit>>

    fun loginUser(data: LoginRequest): Flow<Result<String>>

    fun logoutUser(): Flow<Result<LogoutResponse>>

    fun resend(data: ResendRequest): Flow<Result<BaseResponse>>

    fun reset(data: ResetUserRequest): Flow<Result<BaseResponse>>

    fun setNewPassword(data: NewPasswordRequest): Flow<Result<BaseResponse>>

    fun refresh(data: RefreshRequest): Flow<Result<RefreshResponse>>

    fun getUserPhoneNumber(): String

    fun setUserToken(token: String)

    fun putUserAvatar(data: File): Flow<Result<BaseResponse>>

    fun getUserAvatar(): Flow<Result<AvatarResponse>>

    fun putUserInfo(data: UserInfoRequest): Flow<Result<BaseResponse>>

    fun getProfileInfo(): Flow<Result<ProfileInfoResponse>>

    fun getUserFullName() : String
}