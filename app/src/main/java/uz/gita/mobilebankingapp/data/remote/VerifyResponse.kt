package uz.gita.mobilebankingapp.data.remote

import com.google.gson.annotations.SerializedName

data class VerifyResponse(
    val data : TokenData
)

data class TokenData(
    @SerializedName("access_token")
    val accessToken : String,

    @SerializedName("refresh_token")
    val refreshToken : String,
)