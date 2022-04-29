package uz.gita.mobilebankingapp.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse

interface HistoryRepository {
    fun getHistoryPagingData(scope: CoroutineScope): Flow<PagingData<MoneyTransferResponse.HistoryData>>

    fun getHistoryDataCount() : Int
}