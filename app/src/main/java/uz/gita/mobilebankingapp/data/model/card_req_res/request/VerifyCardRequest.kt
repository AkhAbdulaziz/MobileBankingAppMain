package uz.gita.mobilebankingapp.data.model.card_req_res.request

data class VerifyCardRequest(
    val pan: String,
    val code: String
)