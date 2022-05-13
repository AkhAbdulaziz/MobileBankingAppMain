package uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import uz.targetsoftwaredevelopment.mobilebankingapp.data.datasource.HistoryDataSource
import uz.targetsoftwaredevelopment.mobilebankingapp.data.local.LocalStorage
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api.MoneyTransferApi
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val localStorage: LocalStorage,
    val api: MoneyTransferApi
) : HistoryRepository {
    private val config = PagingConfig(10)

    override fun getHistoryPagingData(scope: CoroutineScope): Flow<PagingData<MoneyTransferResponse.HistoryData>> =
        Pager(config) {
            HistoryDataSource(api, localStorage)
        }.flow.cachedIn(scope)

    override fun getHistoryDataCount(): Int {
        return localStorage.historyDataCount
    }

    override fun getIncomes(): String {
        return localStorage.incomes
    }

    override fun getExpenditures(): String {
        return localStorage.expenditures
    }
}