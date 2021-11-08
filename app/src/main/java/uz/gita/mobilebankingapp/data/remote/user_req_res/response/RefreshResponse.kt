package uz.gita.mobilebankingapp.data.remote.user_req_res.response

data class RefreshResponse(
    val access_token: String,
    val refresh_token: String
)