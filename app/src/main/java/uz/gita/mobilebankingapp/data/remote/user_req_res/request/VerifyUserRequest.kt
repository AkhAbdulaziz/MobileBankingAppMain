package uz.gita.mobilebankingapp.data.remote.user_req_res.request

data class VerifyUserRequest(
    val phone: String,
    val code: String
)