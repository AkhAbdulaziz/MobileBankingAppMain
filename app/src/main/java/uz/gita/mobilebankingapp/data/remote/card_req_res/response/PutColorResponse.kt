package uz.gita.mobilebankingapp.data.remote.card_req_res.response


data class PutColorResponse(
    val data: Data? = null
)

data class PutColorResponseData(
    val color: Int? = null,
    val userCardId: Int? = null
)
