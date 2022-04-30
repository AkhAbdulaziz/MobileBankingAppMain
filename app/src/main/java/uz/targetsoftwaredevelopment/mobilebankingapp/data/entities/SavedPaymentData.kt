package uz.targetsoftwaredevelopment.mobilebankingapp.data.entities

import java.io.Serializable

data class SavedPaymentData(
    var isLast: Boolean = true,
    var logo: Int,
    var title: String,
    var number: String
) : Serializable
