package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res

import java.io.Serializable

data class CardData(
    val id: Int? = null,
    val pan: String? = null,
    val exp: String? = null,
    val owner: String? = null,
    val cardName: String? = null,
    val balance: Int? = null,
    val color: Int? = null,
    val ignoreBalance: Boolean = false,
    val status: Int? = null
) : Serializable


