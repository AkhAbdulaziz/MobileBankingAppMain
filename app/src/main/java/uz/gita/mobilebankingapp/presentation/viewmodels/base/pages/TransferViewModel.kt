package uz.gita.mobilebankingapp.presentation.viewmodels.base.pages

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest

interface TransferViewModel {
    val enableNextButton : LiveData<Unit>
    val errorLiveData : LiveData<String>
    val successLiveData : LiveData<String>
    val ownerNameLiveData : LiveData<String>
    val openLoginScreenLiveData: LiveData<Unit>

    fun getOwnerByPan(data : OwnerByPanRequest)
}