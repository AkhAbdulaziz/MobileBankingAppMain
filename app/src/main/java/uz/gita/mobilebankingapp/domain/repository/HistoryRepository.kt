package uz.gita.mobilebankingapp.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse

interface HistoryRepository {
    fun getHistoryPagingData(scope: CoroutineScope): kotlinx.coroutines.flow.Flow<PagingData<MoneyTransferResponse.HistoryData>>
}