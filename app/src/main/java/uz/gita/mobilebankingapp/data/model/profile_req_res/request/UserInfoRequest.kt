package uz.gita.mobilebankingapp.data.model.profile_req_res.request

data class UserInfoRequest(
    val firstName: String?,
    val lastName: String?,
    val password: String = "qwertyuiop"
)
