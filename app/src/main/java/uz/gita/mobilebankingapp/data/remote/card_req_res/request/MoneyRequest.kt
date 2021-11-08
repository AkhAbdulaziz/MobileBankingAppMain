package uz.gita.mobilebankingapp.data.remote.card_req_res.request

data class MoneyRequest(
    val sender: Int,
    val receiverPan: String,
    val amount: Long
)