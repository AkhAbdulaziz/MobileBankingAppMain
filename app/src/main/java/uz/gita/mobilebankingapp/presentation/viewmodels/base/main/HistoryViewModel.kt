package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse

interface HistoryViewModel {
    fun getHistoryPagingData(): Flow<PagingData<MoneyTransferResponse.HistoryData>>
}