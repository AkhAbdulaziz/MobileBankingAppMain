package uz.gita.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.AddCardRequest

interface AddCardViewModel {
    val enableAddButtonLiveData: LiveData<Unit>
    val disableAddButtonLiveData: LiveData<Unit>
    val openVerifyCardScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>
    val closeScreenLiveData: LiveData<Unit>
    val openLoginScreenLiveData: LiveData<Unit>

    fun addCard(cardData: AddCardRequest)
}