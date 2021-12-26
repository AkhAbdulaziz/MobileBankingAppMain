package uz.gita.mobilebankingapp.data

import java.io.Serializable

data class CheckData(
    val receiverPan: String,
    val receiverName: String,
    val time: Long,
    val senderPan: String,
    val fee: Float,
    val totalCost: Float
) : Serializable