package uz.gita.mobilebankingapp.domain.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import uz.gita.mobilebankingapp.data.datasource.HistoryDataSource
import uz.gita.mobilebankingapp.data.local.MySharedPreferences
import uz.gita.mobilebankingapp.data.remote.api.MoneyTransferApi
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.gita.mobilebankingapp.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val pref: MySharedPreferences,
    val api: MoneyTransferApi
) : HistoryRepository {
    private val config = PagingConfig(10)

    override fun getHistoryPagingData(scope: CoroutineScope): Flow<PagingData<MoneyTransferResponse.HistoryData>> =
        Pager(config) {
            HistoryDataSource(api, pref)
        }.flow.cachedIn(scope)
}