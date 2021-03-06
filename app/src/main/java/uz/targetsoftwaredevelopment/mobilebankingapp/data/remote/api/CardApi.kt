package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api

import retrofit2.Response
import retrofit2.http.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.BaseResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.UserMessageResponse

interface CardApi {

    @POST("card/add-card")
    suspend fun addCard(
        @Body data: AddCardRequest
    ): Response<VerifyCardResponse>

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
        @Header("token") token: String
    ): Response<AllCardsResponse>

    //    @HTTP(method = "GET", path = "card/owner-by-pan", hasBody = true)
    @GET("card/owner-by-pan")
    suspend fun getOwnerByPan(
        @Query("pan") pan: String
//        @Body data: OwnerByPanRequest
    ): Response<OwnerByPanResponse>

    //    @HTTP(method = "GET", path = "card/owner-by-id", hasBody = true)
    @GET("card/owner-by-id")
    suspend fun getOwnerById(
        @Query("id") id: Int
//        @Body data: OwnerByIdRequest
    ): Response<OwnerByIdResponse>

    //    @HTTP(method = "GET", path = "card/pan-by-id", hasBody = true)
    @GET("card/pan-by-id")
    suspend fun getPanById(
        @Query("id") id: Int
//        @Body data: PanByIdRequest
    ): Response<PanByIdResponse>

    /* fun groupList(
         @Path("id") groupId: Int
     ): List<User?>?*/

    @POST("money-transfer/send-money")
    suspend fun sendMoney(
        @Body data: MoneyRequest
    ): Response<SendMoneyResponse>

    @GET("money-transfer/fee")
    suspend fun getFee(
        @Query("sender") sender: Int,
        @Query("receiverPan") receiverPan: String,
        @Query("amount") amount: Long
//        @Body data: MoneyRequest
    ): Response<BaseResponse>

    /**
    val sender: Int,
    val receiverPan: String,
    val amount: Long
     **/

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
    ): Response<PutColorResponse>

    @PUT("card/ignore-balance")
    suspend fun putIgnoreBalance(
        @Body data: IgnoreBalanceRequest
    ): Response<String>

    @GET("card/total-sum")
    suspend fun getTotalSum(): Response<TotalCardResponse>
}