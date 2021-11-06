package uz.gita.mobilebankingapp.data.model.card_req_res.response

import uz.gita.mobilebankingapp.data.CardData

data class AllCardsResponse(
    val data: GetCardsData? = null
)

data class GetCardsData(
    val data: List<CardData>? = null
)