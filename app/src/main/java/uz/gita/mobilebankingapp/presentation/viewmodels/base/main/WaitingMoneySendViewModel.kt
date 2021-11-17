package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.ReceiptMoneyData

interface WaitingMoneySendViewModel {
    val errorLiveData: LiveData<String>
    val successLiveData: LiveData<ReceiptMoneyData>

    fun sendMoney(data: MoneyRequest)

}