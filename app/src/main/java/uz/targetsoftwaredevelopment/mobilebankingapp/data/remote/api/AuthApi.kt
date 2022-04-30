package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.*

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body data: RegisterRequest): Response<UserMessageResponse>

    @POST("auth/verify")
    suspend fun verifyUser(
        @Body data: VerifyUserRequest
    ): Response<VerifyUserResponse>

    @POST("auth/login")
    suspend fun login(@Body data: LoginRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("token") token: String): Response<LogoutResponse>

    @POST("auth/resend")
    suspend fun resend(
        @Body data: ResendRequest
    ): Response<BaseResponse>

    @POST("auth/reset")
    suspend fun reset(
        @Body data: ResetUserRequest
    ): Response<BaseResponse>

    @POST("auth/newpassword")
    suspend fun setNewPassword(
        @Body data: NewPasswordRequest
    ): Response<BaseResponse>

    @POST("auth/refresh")
    suspend fun refresh(
        @Body data: RefreshRequest
    ): Response<RefreshResponse>
}
