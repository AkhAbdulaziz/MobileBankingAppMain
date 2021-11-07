package uz.gita.mobilebankingapp.data

import java.io.Serializable

data class CardData(
    val id: Int? = null,
    val owner: String? = null,
    val cardName: String? = null,
    val balance: Int? = null,
    val pan: String? = null,
    val exp: String? = null,
    val status: Int? = null
) : Serializable
