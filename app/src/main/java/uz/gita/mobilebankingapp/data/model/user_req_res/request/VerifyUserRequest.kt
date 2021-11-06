package uz.gita.mobilebankingapp.data.model.user_req_res.request

data class VerifyUserRequest(
    val phone: String,
    val code: String
)