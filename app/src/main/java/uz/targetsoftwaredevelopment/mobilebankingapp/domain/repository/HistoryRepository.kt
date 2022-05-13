package uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse

interface HistoryRepository {
    fun getHistoryPagingData(scope: CoroutineScope): Flow<PagingData<MoneyTransferResponse.HistoryData>>

    fun getHistoryDataCount(): Int

    fun getIncomes(): String

    fun getExpenditures(): String
}