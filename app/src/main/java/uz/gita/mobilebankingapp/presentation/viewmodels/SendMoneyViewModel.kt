package uz.gita.mobilebankingapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.model.card_req_res.request.MoneyRequest

interface SendMoneyViewModel {
    val enableSendMoneyButton : LiveData<Unit>
    val errorLiveData : LiveData<String>
    val successLiveData : LiveData<String>

    fun sendMoney(data : MoneyRequest)
}