package uz.targetsoftwaredevelopment.mobilebankingapp.data.entities

import java.io.Serializable

data class SavedPaymentData(
    var isLast: Boolean = true,
    var logo: Int,
    var logoName: String,
    var title: String,
    var phone: String,
    var moneyAmount: Int = 0
) : Serializable
