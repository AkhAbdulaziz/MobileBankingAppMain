package uz.gita.mobilebankingapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.*
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.GetCardsData
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.OwnerByIdResponse
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.OwnerByPanResponse
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.VerifyCardResponseData
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.BaseResponse

interface CardRepository {
    fun addCard(data: AddCardRequest): Flow<Result<String>>

    fun getAllCardsList(token: String): Flow<Result<GetCardsData?>?>

    fun sendMoney(data: MoneyRequest): Flow<Result<String>>

    fun verifyCard(data: VerifyCardRequest): Flow<Result<VerifyCardResponseData>>

    fun savePanToPref(pan: String)

    fun getCurrentPan(): String

    fun deleteCard(data: DeleteCardRequest): Flow<Result<String>>

    fun editCard(data: EditCardRequest): Flow<Result<String>>

    fun getOwnerByPan(data: OwnerByPanRequest): Flow<Result<OwnerByPanResponse>>

    fun getOwnerById(data: OwnerByIdRequest): Flow<Result<OwnerByIdResponse>>

    fun getFee(data: MoneyRequest): Flow<Result<BaseResponse>>

    fun getHistory(data: HistoryRequest): Flow<Result<BaseResponse>>

    fun getIncomes(data: HistoryRequest): Flow<Result<BaseResponse>>

    fun getOutcomes(data: HistoryRequest): Flow<Result<BaseResponse>>

    fun putColor(data: ColorRequest): Flow<Result<String>>

    fun putIgnoreBalance(data: IgnoreBalanceRequest): Flow<Result<String>>

    fun getTotalSum(): Flow<Result<Double>>

    fun getUserCardDataByPan(pan: String): CardData?
}