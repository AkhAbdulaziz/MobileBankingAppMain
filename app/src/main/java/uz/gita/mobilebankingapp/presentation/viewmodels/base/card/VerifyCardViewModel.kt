package uz.gita.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.VerifyCardRequest

interface VerifyCardViewModel {
    val errorMessageLiveData: LiveData<String>
    val openMyCardsScreenLiveData: LiveData<Unit>

    fun verifyCard(data: VerifyCardRequest)

    fun getCurrentPan() : String

    fun getUserPhoneNumber(): String
}