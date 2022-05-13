package uz.targetsoftwaredevelopment.mobilebankingapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.targetsoftwaredevelopment.mobilebankingapp.data.local.LocalStorage
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api.MoneyTransferApi
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.timber

class HistoryDataSource(
    private val api: MoneyTransferApi,
    private val localStorage: LocalStorage
) : PagingSource<Int, MoneyTransferResponse.HistoryData>() {
    private var incomeCounter: Float = 0f
    private var expenditureCounter: Float = 0f

    override fun getRefreshKey(state: PagingState<Int, MoneyTransferResponse.HistoryData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoneyTransferResponse.HistoryData> {
        return try {
            val nextPageNumber = params.key ?: 0
            val pageSize = 10
            val response =
                api.getMoneyTransferHistory(localStorage.accessToken, nextPageNumber, pageSize)

            localStorage.historyDataCount = response.body()!!.data.totalCount

            for (historyData in response.body()!!.data.data) {
                if (historyData.status == 0) {
                    incomeCounter += historyData.amount
                } else {
                    expenditureCounter += historyData.amount + historyData.fee
                }
            }
            localStorage.incomes = getPortableAmount("${incomeCounter.toInt()}")
            localStorage.expenditures = getPortableAmount("${expenditureCounter.toInt()}")

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

    private fun getPortableAmount(amount: String): String {
        var firstPiece = ""
        var secondPiece = ""
        var thirdPiece = ""

        if (amount.length <= 3) {
            firstPiece = amount
        } else if (amount.length <= 6) {
            secondPiece = amount.substring(amount.length - 3)
            firstPiece = amount.substring(0, amount.length - 3)
        } else if (amount.length <= 9) {
            thirdPiece = amount.substring(amount.length - 3)
            secondPiece = amount.substring(amount.length - 6, amount.length - 3)
            firstPiece = amount.substring(0, amount.length - 6)
        }

        return "$firstPiece $secondPiece $thirdPiece".trim()
    }
}