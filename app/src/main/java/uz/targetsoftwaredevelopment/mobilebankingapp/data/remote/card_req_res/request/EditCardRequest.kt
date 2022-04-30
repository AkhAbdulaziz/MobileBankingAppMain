package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request

data class EditCardRequest(
    val cardNumber: String,
    val newName: String
)