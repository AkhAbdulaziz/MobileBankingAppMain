package uz.gita.mobilebankingapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.model.card_req_res.request.AddCardRequest

interface AddCardViewModel {
    val enableAddButtonLiveData: LiveData<Unit>
    val disableAddButtonLiveData: LiveData<Unit>
    val openVerifyCardScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>

    fun addCard(cardData: AddCardRequest)

}