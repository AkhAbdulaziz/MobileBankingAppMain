package uz.gita.mobilebankingapp.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.mobilebankingapp.data.local.MySharedPreferences
import uz.gita.mobilebankingapp.data.remote.api.CardApi
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.*
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.*
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.BaseResponse
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.utils.timber
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardApi: CardApi,
    private val pref: MySharedPreferences
) : CardRepository {
    private val gson = Gson()
    private var cardsList: List<CardData>? = null

    private var cardVerifiedListener: (() -> Unit)? = null
    override fun setCardVerifiedListener(block: () -> Unit) {
        cardVerifiedListener = block
    }

    override fun addCard(data: AddCardRequest): Flow<Result<String>> = flow {
        savePanToPref(data.pan)
        val response = cardApi.addCard(data)
        if (response.isSuccessful) {
            getAllCardsList()
            if (cardsList!!.size == 1) {
                pref.mainCardPan = data.pan
            }
            emit(Result.success(response.body()!!.message))
        } else {
            var st = "Serverga ulanishda xatolik bo'ldi"

            response.errorBody()?.let {
                st = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure<String>(Throwable(st)))
        }

    }.catch { throwable ->
        timber(throwable.message.toString())
    }.flowOn(Dispatchers.IO)

    override fun getAllCardsList(): Flow<Result<GetCardsData?>?> = flow {
        val response = cardApi.getAllCards(pref.accessToken)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.data))
            cardsList = response.body()!!.data?.data
        }
    }.flowOn(Dispatchers.IO)

    override fun sendMoney(data: MoneyRequest): Flow<Result<ReceiptMoneyData>> = flow {
        val response = cardApi.sendMoney(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.data))
        }
        /* else {
                var st = "Serverga ulanishda xatolik bo'ldi"
                ResponseBody
                response.errorBody()?.let {
                    st = gson.fromJson(it.string(), SendMoneyResponse::class.java).data
                }
                emit(Result.failure<String>(Throwable(st)))
            }*/

    }.flowOn(Dispatchers.IO)

    override fun verifyCard(data: VerifyCardRequest): Flow<Result<VerifyCardResponseData>> = flow {
        val response = cardApi.verifyCard(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.data))
            cardVerifiedListener?.invoke()
        }

    }.flowOn(Dispatchers.IO)

    override fun savePanToPref(pan: String) {
        pref.currentPan = pan
    }

    override fun getCurrentPan(): String {
        return pref.currentPan
    }

    override fun deleteCard(data: DeleteCardRequest): Flow<Result<String>> = flow {
        val response = cardApi.deleteCard(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun editCard(data: EditCardRequest): Flow<Result<String>> = flow {
        val response = cardApi.editCard(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun getOwnerByPan(data: OwnerByPanRequest): Flow<Result<OwnerByPanResponse>> = flow {
        val response = cardApi.getOwnerByPan(data.pan)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getOwnerById(data: OwnerByIdRequest): Flow<Result<OwnerByIdResponse>> = flow {
        val response = cardApi.getOwnerById(data.id)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getPanById(data: PanByIdRequest): Flow<Result<PanByIdResponse>> = flow {
        val response = cardApi.getPanById(data.id)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFee(data: MoneyRequest): Flow<Result<BaseResponse>> = flow {
        val response = cardApi.getFee(data.sender, data.receiverPan, data.amount)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getHistory(data: HistoryRequest): Flow<Result<BaseResponse>> = flow {
        val response = cardApi.getHistory(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getIncomes(data: HistoryRequest): Flow<Result<BaseResponse>> = flow {
        val response = cardApi.getIncomes(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getOutcomes(data: HistoryRequest): Flow<Result<BaseResponse>> = flow {
        val response = cardApi.getOutcomes(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun putColor(data: ColorRequest): Flow<Result<String>> = flow {
        val response = cardApi.putColor(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun putIgnoreBalance(data: IgnoreBalanceRequest): Flow<Result<String>> = flow {
        val response = cardApi.putIgnoreBalance(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getTotalSumFromLocal(): String {
        return pref.lastAllMoneyAmount
    }

    override fun getTotalSum(): Flow<Result<TotalCardResponse>> = flow {
        val response = cardApi.getTotalSum()
        if (response.isSuccessful) {
            pref.lastAllMoneyAmount = "${response.body()!!.data}"
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getMyMainCardData(): CardData? {
        getAllCardsList()
        if (cardsList != null) {
            for (cardData: CardData in cardsList!!) {
                if (cardData.pan.equals(pref.mainCardPan)) {
                    return cardData
                }
            }
            return cardsList!![0]
        }
        return null
    }

    override fun changeMainCard(pan: String) {
        if (pref.mainCardPan == pan) {
            pref.mainCardPan = cardsList!![0].pan!!
        } else {
            pref.mainCardPan = pan
        }
    }

    override var isBalanceVisible: Boolean
        get() = pref.isBalanceVisible
        set(visibility) {
            pref.isBalanceVisible = visibility
            if (cardsList != null) {
                putIgnoreBalance(
                    IgnoreBalanceRequest(
                        getMyMainCardData()!!.id!!,
                        visibility
                    )
                )
            }
        }
}
