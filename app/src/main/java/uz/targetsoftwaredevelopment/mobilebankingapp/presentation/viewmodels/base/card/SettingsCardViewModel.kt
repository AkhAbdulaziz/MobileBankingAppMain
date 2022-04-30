package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.EditCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse

interface SettingsCardViewModel {
    val closeScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>
    val openLoginScreenLiveData: LiveData<LogoutResponse>

    fun editCard(data: EditCardRequest, cardId: Int, bgColor: Int)

    fun getMainCardData(): CardData?

    fun changeMainCard(pan: String)
}