package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response

data class SendMoneyResponse(
    val data: ReceiptMoneyData
)

data class ReceiptMoneyData(
    val id: Int,
    val sender: Int,
    val receiver: Int,
    val amount: Double,
    val time: Long,
    val fee: Double,
    val status: Int
)