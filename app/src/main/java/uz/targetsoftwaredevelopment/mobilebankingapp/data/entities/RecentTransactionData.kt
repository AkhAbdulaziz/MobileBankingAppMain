package uz.targetsoftwaredevelopment.mobilebankingapp.data.entities

import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.TransactionTypes
import java.io.Serializable

data class RecentTransactionData(
    var bankLogo: Int? = null,
    var bankName: String,
    var transactionDate: String,
    var transactionTime: String,
    var transactionAmount: String,
    var transactionType: TransactionTypes
) : Serializable
