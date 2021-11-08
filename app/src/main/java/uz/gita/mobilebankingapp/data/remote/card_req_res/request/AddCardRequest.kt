package uz.gita.mobilebankingapp.data.remote.card_req_res.request

data class AddCardRequest(
    val pan: String,
    val exp: String,
    val cardName: String
)