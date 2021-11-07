package uz.gita.mobilebankingapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.model.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.data.model.card_req_res.request.OwnerByPanRequest

interface FillSendMoneyViewModel {
    val enableNextButton : LiveData<Unit>
    val errorLiveData : LiveData<String>
    val successLiveData : LiveData<String>
    val ownerNameLiveData : LiveData<String>

    fun getOwnerByPan(data : OwnerByPanRequest)
}