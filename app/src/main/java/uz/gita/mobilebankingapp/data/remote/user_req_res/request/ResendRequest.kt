package uz.gita.mobilebankingapp.data.remote.user_req_res.request

data class ResendRequest(
    val phone: String,
    val password: String
)