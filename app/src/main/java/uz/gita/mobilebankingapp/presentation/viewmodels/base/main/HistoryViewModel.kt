package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByIdRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.PanByIdRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.PanByIdResponse

interface HistoryViewModel {
    val senderPanByIdLiveData: LiveData<String>
    val receiverPanByIdLiveData: LiveData<String>
    val errorLiveData: LiveData<String>
    val ownerNameLiveData : LiveData<String>
    val historyPagingLiveData : LiveData<PagingData<MoneyTransferResponse.HistoryData>>
    val historyDataCountLiveData : LiveData<Int>

    fun getSenderPanById(data: PanByIdRequest)
    fun getReceiverPanById(data: PanByIdRequest)
    fun getOwnerById(data : OwnerByIdRequest)

    fun getHistoryPagingData()
    fun getHistoryDataCount()
}