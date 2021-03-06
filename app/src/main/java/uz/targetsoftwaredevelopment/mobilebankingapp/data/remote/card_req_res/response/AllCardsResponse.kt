package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response

import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData

data class AllCardsResponse(
    val data: GetCardsData? = null
)

data class GetCardsData(
    val data: List<CardData>? = null
)