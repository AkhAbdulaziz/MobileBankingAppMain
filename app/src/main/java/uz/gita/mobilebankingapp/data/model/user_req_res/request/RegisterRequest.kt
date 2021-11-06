package uz.gita.mobilebankingapp.data.model.user_req_res.request

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val password: String,
    val status: Int = 0
)