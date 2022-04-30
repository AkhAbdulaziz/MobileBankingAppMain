package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.request.UserInfoRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.AvatarResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.BaseResponse
import java.io.File

interface ProfileApi {
    @PUT("profile/avatar")
    suspend fun putUserAvatar(
        @Body avatar: File
    ): Response<BaseResponse>

    @GET("profile/avatar")
    suspend fun getUserAvatar(
    ): Response<AvatarResponse>

    @PUT("profile")
    suspend fun putUserInfo(
        @Body data: UserInfoRequest
    ): Response<BaseResponse>

    @GET("profile/info")
    suspend fun getProfileInfo(
    ) :  Response<ProfileInfoResponse>
}