package uz.gita.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.ReceiptMoneyData

interface SendMoneyViewModel {
    val enableSendMoneyButton: LiveData<Unit>
    val errorLiveData: LiveData<String>
    val successLiveData: LiveData<ReceiptMoneyData>
    val ownerNameLiveData: LiveData<String>
    val feeLiveData: LiveData<String>
    val cardsListLiveData: LiveData<List<CardData>>

    fun sendMoney(data: MoneyRequest)

    fun getOwnerByPan(data: OwnerByPanRequest)

    fun getFee(data: MoneyRequest)

    fun getMyMainCardData(): CardData?

    fun getAllCardList()
}