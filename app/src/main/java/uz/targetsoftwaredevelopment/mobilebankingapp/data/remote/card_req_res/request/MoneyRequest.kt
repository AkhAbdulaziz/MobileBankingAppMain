package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request

import java.io.Serializable

data class MoneyRequest(
    val sender: Int,
    val receiverPan: String,
    val amount: Long
) : Serializable