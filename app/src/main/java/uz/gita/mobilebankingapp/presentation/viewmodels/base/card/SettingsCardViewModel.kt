package uz.gita.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.EditCardRequest

interface SettingsCardViewModel {
    val closeScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>

    fun editCard(data: EditCardRequest)

    fun getMainCardData(): CardData?

    fun changeMainCard(pan: String)
}