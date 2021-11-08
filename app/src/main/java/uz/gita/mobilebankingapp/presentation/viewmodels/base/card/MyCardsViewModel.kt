package uz.gita.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.DeleteCardRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.GetCardsData

interface MyCardsViewModel {
    val cardsListLiveData: LiveData<GetCardsData>
    val errorMessageLiveData: LiveData<String>
    val closeDialogLiveData: LiveData<Unit>

    fun getAllCardList()

    fun deleteCard(data: DeleteCardRequest)
}