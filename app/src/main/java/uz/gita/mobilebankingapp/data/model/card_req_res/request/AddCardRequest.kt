package uz.gita.mobilebankingapp.data.model.card_req_res.request

data class AddCardRequest(
    val pan: String,
    val exp: String,
    val cardName: String
)