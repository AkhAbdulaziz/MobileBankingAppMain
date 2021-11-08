package uz.gita.mobilebankingapp.domain.repository.impl

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import uz.gita.mobilebankingapp.data.local.MySharedPreferences
import uz.gita.mobilebankingapp.data.remote.api.CardApi
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.*
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.GetCardsData
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.OwnerByIdResponse
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.OwnerByPanResponse
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.VerifyCardResponseData
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.BaseResponse
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardApi: CardApi,
    private val pref: MySharedPreferences
) : CardRepository {
    private val gson = Gson()
    private var cardsList: List<CardData>? = null

    override fun addCard(data: AddCardRequest): Flow<Result<String>> = flow {
        savePanToPref(data.pan)
        val response = cardApi.addCard(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.message))
        } else {
            var st = "Serverga ulanishda xatolik bo'ldi"
            ResponseBody
            response.errorBody()?.let {
                st = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure<String>(Throwable(st)))
        }

    }.flowOn(Dispatchers.IO)

    override fun getAllCardsList(token: String): Flow<Result<GetCardsData?>?> = flow {
        val response = cardApi.getAllCards(pref.accessToken)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.data))
            cardsList = response.body()!!.data?.data
        }
    }.flowOn(Dispatchers.IO)

    override fun sendMoney(data: MoneyRequest): Flow<Result<String>> = flow {
        val response = cardApi.sendMoney(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.message))
        } else {
            var st = "Serverga ulanishda xatolik bo'ldi"
            ResponseBody
            response.errorBody()?.let {
                st = gson.fromJson(it.string(), BaseResponse::class.java).message
            }
            emit(Result.failure<String>(Throwable(st)))
        }

    }.flowOn(Dispatchers.IO)

    override fun verifyCard(data: VerifyCardRequest): Flow<Result<VerifyCardResponseData>> = flow {
        val response = cardApi.verifyCard(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!.data))
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
        val response = cardApi.getOwnerByPan(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getOwnerById(data: OwnerByIdRequest): Flow<Result<OwnerByIdResponse>> = flow {
        val response = cardApi.getOwnerById(data)
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFee(data: MoneyRequest): Flow<Result<BaseResponse>> = flow {
        val response = cardApi.getFee(data)
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

    override fun getTotalSum(): Flow<Result<Double>> = flow {
        val response = cardApi.getTotalSum()
        if (response.isSuccessful) {
            emit(Result.success(response.body()!!))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserCardDataByPan(pan: String): CardData? {
        getAllCardsList(pref.accessToken)
        if (cardsList != null) {
            for (cardData: CardData in cardsList!!) {
                if (cardData.pan.equals(pan)) {
                    return cardData
                }
            }
        }
        return null
    }
}
