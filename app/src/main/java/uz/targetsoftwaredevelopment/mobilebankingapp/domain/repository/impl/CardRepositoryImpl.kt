package uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.local.LocalStorage
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.api.CardApi
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.BaseResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.timber
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardApi: CardApi,
    private val localStorage: LocalStorage
) : CardRepository {
    private val gson = Gson()
    private var cardsList: List<CardData>? = null

    private var cardVerifiedListener: (() -> Unit)? = null
    override fun setCardVerifiedListener(block: () -> Unit) {
        cardVerifiedListener = block
    }

    private var panToTransferPageListener: ((String) -> Unit)? = null
    override fun setPanToTransferPageListener(block: (String) -> Unit) {
        panToTransferPageListener = block
    }

    private var dataToHistoryPageListener: ((SavedPaymentData) -> Unit)? = null
    override fun setDataToHistoryPageListener(block: (SavedPaymentData) -> Unit) {
        dataToHistoryPageListener = block
    }

    private var scannedCardNumberListener: ((String) -> Unit)? = null
    override fun setScannedCardNumberListener(block: (String) -> Unit) {
        scannedCardNumberListener = block
    }

    override fun addCard(data: AddCardRequest): Flow<Result<VerifyCardResponseData>> = flow {
        savePanToPref(data.pan)
        val response = cardApi.addCard(data)
        if (response.isSuccessful) {
            getAllCardsList()
            if (cardsList!!.size == 1) {
                localStorage.mainCardPan = data.pan
            }
            emit(Result.success(response.body()!!.data))
        } else {
            var st = "Serverga ulanishda xatolik bo'ldi"

            response.errorBody()?.let {
                st = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure(Throwable(st)))
        }
    }.catch { throwable ->
        timber(throwable.message.toString())
    }.flowOn(Dispatchers.IO)

    override fun getAllCardsList(): Flow<Result<GetCardsData?>?> = flow {
        val response = cardApi.getAllCards(localStorage.accessToken)
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
            }
            */
    }.flowOn(Dispatchers.IO)

    override fun verifyCard(data: VerifyCardRequest): Flow<Result<VerifyCardResponseData>> = flow {
        val response = cardApi.verifyCard(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.data))
            cardVerifiedListener?.invoke()
        }
    }.flowOn(Dispatchers.IO)

    override fun savePanToPref(pan: String) {
        localStorage.currentPan = pan
    }

    override fun getCurrentPan(): Flow<Result<String>> = flow {
        emit(Result.success(localStorage.currentPan))
    }.flowOn(Dispatchers.IO)

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

    override fun putColor(data: ColorRequest): Flow<Result<PutColorResponse>> = flow {
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

    override fun getTotalSumFromLocal(): Flow<Result<String>> = flow {
        emit(Result.success(localStorage.lastAllMoneyAmount))
    }.flowOn(Dispatchers.IO)

    override fun getTotalSum(): Flow<Result<String>> = flow {
        val response = cardApi.getTotalSum()
        if (response.isSuccessful) {
            val amount = getPortableAmount("${response.body()!!.data.toInt()}")
            localStorage.lastAllMoneyAmount = amount
            emit(Result.success(amount))
        }
    }.flowOn(Dispatchers.IO)

    override fun getMyMainCardData(): CardData? {
        getAllCardsList()
        if (cardsList != null) {
            for (cardData: CardData in cardsList!!) {
                if (cardData.pan.equals(localStorage.mainCardPan)) {
                    return cardData
                }
            }
            return cardsList!![0]
        }
        return null
    }

    override fun changeMainCard(pan: String) {
        if (localStorage.mainCardPan == pan) {
            localStorage.mainCardPan = cardsList!![0].pan!!
        } else {
            localStorage.mainCardPan = pan
        }
    }

    override var isBalanceVisible: Boolean
        get() = localStorage.isBalanceVisible
        set(visibility) {
            localStorage.isBalanceVisible = visibility
            if (cardsList != null && cardsList!!.isNotEmpty()) {
                putIgnoreBalance(
                    IgnoreBalanceRequest(
                        getMyMainCardData()!!.id!!,
                        visibility
                    )
                )
            }
        }

    override fun givePanToTransferPage(pan: String) {
        panToTransferPageListener?.invoke(pan)
    }

    override fun giveDataToHistoryPage(data: SavedPaymentData) {
        dataToHistoryPageListener?.invoke(data)
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

    override fun saveScannedCardNumber(cardNumber: String) {
        localStorage.scannedCardNumber = cardNumber
        scannedCardNumberListener?.invoke(cardNumber)
    }
}
