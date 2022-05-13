package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.OwnerByIdRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.PanByIdRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse

interface HistoryViewModel {
    val argumentDataLiveData: LiveData<SavedPaymentData>
    val senderPanByIdLiveData: LiveData<String>
    val receiverPanByIdLiveData: LiveData<String>
    val errorLiveData: LiveData<String>
    val ownerNameLiveData: LiveData<String>
    val historyPagingLiveData: LiveData<PagingData<MoneyTransferResponse.HistoryData>>
    val historyDataCountLiveData: LiveData<Int>
    val incomesLiveData: LiveData<String>
    val expendituresLiveData: LiveData<String>

    fun getSenderPanById(data: PanByIdRequest)
    fun getReceiverPanById(data: PanByIdRequest)
    fun getOwnerById(data: OwnerByIdRequest)

    fun getHistoryPagingData()
}