package uz.gita.mobilebankingapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.model.card_req_res.request.EditCardRequest

interface SettingsCardViewModel {
    val closeScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>

    fun editCard(data: EditCardRequest)
}