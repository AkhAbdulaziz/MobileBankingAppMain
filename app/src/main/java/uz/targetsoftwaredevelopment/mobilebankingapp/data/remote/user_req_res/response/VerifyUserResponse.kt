package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response

data class VerifyUserResponse(
    val data: Data?
)

data class Data(
    val access_token: String?,
    val refresh_token: String?
)
