package uz.targetsoftwaredevelopment.mobilebankingapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.targetsoftwaredevelopment.mobilebankingapp.data.local.LocalStorage
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api.MoneyTransferApi
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.timber

class HistoryDataSource(
    private val api: MoneyTransferApi,
    private val pref: LocalStorage
) : PagingSource<Int, MoneyTransferResponse.HistoryData>() {
    override fun getRefreshKey(state: PagingState<Int, MoneyTransferResponse.HistoryData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoneyTransferResponse.HistoryData> {
        return try {
            val nextPageNumber = params.key ?: 0
            val pageSize = 10
            val response = api.getMoneyTransferHistory(pref.accessToken, nextPageNumber, pageSize)

            pref.historyDataCount = response.body()!!.data.totalCount

            timber(response.isSuccessful.toString())
            timber(response.body().toString())

            LoadResult.Page(
                data = response.body()!!.data.data,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < (response.body()!!.data.totalCount / response.body()!!.data.pageSize) + 1)
                    nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            timber("e = $e")
            LoadResult.Error(Throwable("Ulanishda xatolik bo'ldi"))
        }
    }
}