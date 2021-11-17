package uz.gita.mobilebankingapp.data.remote.api

import retrofit2.Response
import retrofit2.http.*
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.*
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.*
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.BaseResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.UserMessageResponse

interface CardApi {
    @POST("card/add-card")
    suspend fun addCard(
        @Body data: AddCardRequest
    ): Response<UserMessageResponse>

    @POST("card/verify")
    suspend fun verifyCard(
        @Body data: VerifyCardRequest
    ): Response<VerifyCardResponse>

    @POST("card/edit-card")
    suspend fun editCard(
        @Body data: EditCardRequest
    ): Response<UserMessageResponse>

    @POST("card/delete-card")
    suspend fun deleteCard(
        @Body data: DeleteCardRequest
    ): Response<UserMessageResponse>

    @GET("card/all")
    suspend fun getAllCards(
    ): Response<AllCardsResponse>


    //    @HTTP(method = "GET", path = "card/owner-by-pan", hasBody = true)
    @GET("card/owner-by-pan")
    suspend fun getOwnerByPan(
        @Body data: OwnerByPanRequest
    ): Response<OwnerByPanResponse>

    @GET("card/owner-by-id")
    suspend fun getOwnerById(
        @Body data: OwnerByIdRequest
    ): Response<OwnerByIdResponse>

    @POST("money-transfer/send-money")
    suspend fun sendMoney(
        @Body data: MoneyRequest
    ): Response<SendMoneyResponse>

    @GET("money-transfer/fee")
    suspend fun getFee(
        @Body data: MoneyRequest
    ): Response<BaseResponse>

    @GET("money-transfer/history")
    suspend fun getHistory(
        @Body data: HistoryRequest
    ): Response<BaseResponse>

    @GET("money-transfer/incomes")
    suspend fun getIncomes(
        @Body data: HistoryRequest
    ): Response<BaseResponse>

    @GET("money-transfer/outcomes")
    suspend fun getOutcomes(
        @Body data: HistoryRequest
    ): Response<BaseResponse>

    @PUT("card/color")
    suspend fun putColor(
        @Body data: ColorRequest
    ): Response<String>

    @PUT("card/ignore-balance")
    suspend fun putIgnoreBalance(
        @Body data: IgnoreBalanceRequest
    ): Response<String>

    @GET("card/total-sum")
    suspend fun getTotalSum(): Response<TotalCardResponse>
}