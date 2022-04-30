package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import uz.targetsoftwaredevelopment.mobilebankingapp.app.App
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData

fun List<CardData>.sortMainCard(mainCard: CardData?): List<CardData> {
    val list = ArrayList<CardData>()
    lateinit var newMainCard: CardData

    if (mainCard != null) {
        for (card: CardData in this) {
            if (card.pan == mainCard.pan) {
                newMainCard = card
                break
            }
        }
        list.add(newMainCard)
        for (card: CardData in this) {
            if (card != newMainCard) {
                list.add(card)
            }
        }
    }
    return list
}

fun Float.toPx(): Int {
    val r: Resources = App.instance.resources
    val px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        r.displayMetrics
    ).toInt()

    return px
}

fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

fun Int.pxToDp(displayMetrics: DisplayMetrics): Int = (this / displayMetrics.density).toInt()