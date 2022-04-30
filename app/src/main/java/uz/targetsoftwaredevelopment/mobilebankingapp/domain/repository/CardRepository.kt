package uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.BaseResponse

interface CardRepository {
    fun setCardVerifiedListener(block: () -> Unit)

    fun addCard(data: AddCardRequest): Flow<Result<VerifyCardResponseData>>

    fun getAllCardsList(): Flow<Result<GetCardsData?>?>

    fun sendMoney(data: MoneyRequest): Flow<Result<ReceiptMoneyData>>

    fun verifyCard(data: VerifyCardRequest): Flow<Result<VerifyCardResponseData>>

    fun savePanToPref(pan: String)

    fun getCurrentPan(): Flow<Result<String>>

    fun deleteCard(data: DeleteCardRequest): Flow<Result<String>>

    fun editCard(data: EditCardRequest): Flow<Result<String>>

    fun getOwnerByPan(data: OwnerByPanRequest): Flow<Result<OwnerByPanResponse>>

    fun getOwnerById(data: OwnerByIdRequest): Flow<Result<OwnerByIdResponse>>

    fun getPanById(data: PanByIdRequest): Flow<Result<PanByIdResponse>>

    fun getFee(data: MoneyRequest): Flow<Result<BaseResponse>>

    fun getHistory(data: HistoryRequest): Flow<Result<BaseResponse>>

    fun getIncomes(data: HistoryRequest): Flow<Result<BaseResponse>>

    fun getOutcomes(data: HistoryRequest): Flow<Result<BaseResponse>>

    fun putColor(data: ColorRequest): Flow<Result<PutColorResponse>>

    fun putIgnoreBalance(data: IgnoreBalanceRequest): Flow<Result<String>>

    fun getTotalSum(): Flow<Result<TotalCardResponse>>

    fun getMyMainCardData(): CardData?

    var isBalanceVisible : Boolean

    fun getTotalSumFromLocal(): Flow<Result<String>>

    fun changeMainCard(pan :String)

    fun setPanToTransferPageListener(block: (String) -> Unit)

    fun givePanToTransferPage(pan: String)
}