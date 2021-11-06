package uz.gita.mobilebankingapp.data.model.card_req_res.request

data class MoneyRequest(
    val sender: Int,
    val receiverPan: String,
    val amount: Long
)