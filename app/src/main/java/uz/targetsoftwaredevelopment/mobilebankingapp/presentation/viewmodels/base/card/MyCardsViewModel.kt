package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.DeleteCardRequest

interface MyCardsViewModel {
    val cardsListLiveData: LiveData<List<CardData>>
    val errorMessageLiveData: LiveData<String>
    val closeDialogLiveData: LiveData<Unit>
    val openLoginScreenLiveData: LiveData<Unit>

    fun getAllCardList()

    fun deleteCard(data: DeleteCardRequest)
}