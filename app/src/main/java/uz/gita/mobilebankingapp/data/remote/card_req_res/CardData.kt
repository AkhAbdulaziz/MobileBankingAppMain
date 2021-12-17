package uz.gita.mobilebankingapp.data.remote.card_req_res

import uz.gita.mobilebankingapp.R
import java.io.Serializable

data class CardData(
    val id: Int? = null,
    val pan: String? = null,
    val exp: String? = null,
    val owner: String? = null,
    val cardName: String? = null,
    val balance: Int? = null,
    val color: Int = R.color.default_card_bg_color,
    val ignoreBalance: Boolean = false,
    val status: Int? = null

) : Serializable

