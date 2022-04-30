package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response

data class ProfileInfoResponse(
    val data: Data? = null
)

data class Data(
    val firstName: String? = null,
    val lastName: String? = null,
    val password: String? = null,
    val phone: String? = null,
    var username: String = ""
)
