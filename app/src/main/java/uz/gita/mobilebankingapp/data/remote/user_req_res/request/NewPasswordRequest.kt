package uz.gita.mobilebankingapp.data.remote.user_req_res.request

data class NewPasswordRequest(
    val phone: String,
    val newPassword: String,
    val code: String
)