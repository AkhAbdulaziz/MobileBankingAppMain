package uz.gita.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.DeleteCardRequest

interface MyCardsViewModel {
    val cardsListLiveData: LiveData<List<CardData>>
    val errorMessageLiveData: LiveData<String>
    val closeDialogLiveData: LiveData<Unit>

    fun getAllCardList()

    fun deleteCard(data: DeleteCardRequest)
}