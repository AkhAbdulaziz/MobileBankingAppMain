package uz.gita.mobilebankingapp.data.model.card_req_res.response

data class VerifyCardResponse(
    val data: VerifyCardResponseData
)

data class VerifyCardResponseData(
    val id: Int,
    val pan: String,
    val exp: String,
    val owner: String,
    val cardName: String,
    val balance: Float,
    val status: Int? = null
)