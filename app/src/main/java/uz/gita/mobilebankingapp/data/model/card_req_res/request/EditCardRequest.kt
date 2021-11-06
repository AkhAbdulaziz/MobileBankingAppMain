package uz.gita.mobilebankingapp.data.model.card_req_res.request

data class EditCardRequest(
    val cardNumber: String,
    val newName: String
)