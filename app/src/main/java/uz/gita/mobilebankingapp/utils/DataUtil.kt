package uz.gita.mobilebankingapp.utils

import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData

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