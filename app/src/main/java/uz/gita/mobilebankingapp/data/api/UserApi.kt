package uz.gita.mobilebankingapp.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import uz.gita.mobilebankingapp.data.model.VerifyResponse
import uz.gita.mobilebankingapp.data.model.user_req_res.request.*
import uz.gita.mobilebankingapp.data.model.user_req_res.response.*

interface UserApi {
    @POST("auth/register")
    suspend fun register(@Body data: RegisterRequest): Response<UserMessageResponse>

    @POST("auth/verify")
    suspend fun verifyUser(
        @Body data: VerifyUserRequest
    ): Response<VerifyResponse>

    @POST("auth/login")
    suspend fun login(@Body data: LoginRequest): Response<UserMessageResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<LogoutResponse>

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
