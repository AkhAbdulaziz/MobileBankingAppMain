package uz.gita.mobilebankingapp.data.remote.user_req_res.request

data class LoginRequest(
    val phone:String,
    val password:String
)