package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse

interface MoneyTransferApi {

    @GET("money-transfer/history")
    suspend fun getMoneyTransferHistory(
        @Header("token") token: String,
        @Query("page_number") pageNumber: Int,
        @Query("page_size") pageSize: Int
    ): Response<MoneyTransferResponse.TransferResponse>
}