package uz.gita.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SendMoneyViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val authRepository: AuthRepository
) :
    ViewModel(), SendMoneyViewModel {
    override val enableSendMoneyButton = MutableLiveData<Unit>()
    override val errorLiveData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<String>()
    override val ownerNameLiveData = MutableLiveData<String>()
    override val feeLiveData = MutableLiveData<String>()

    override fun getOwnerByPan(data: OwnerByPanRequest) {
        checkInternet()
        cardRepository.getOwnerByPan(data).onEach {
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess { data ->
                enableSendMoneyButton.value = Unit
                ownerNameLiveData.value = data.owner
            }
        }.launchIn(viewModelScope)
    }

    override fun sendMoney(data: MoneyRequest) {
        checkInternet()
        cardRepository.sendMoney(data).onEach {
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess { message ->
                successLiveData.value = message
            }
        }.launchIn(viewModelScope)
    }

    override fun getFee(data: MoneyRequest) {
        checkInternet()
        cardRepository.getFee(data).onEach {
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess {
                feeLiveData.value = it.message
            }
        }
    }

    private fun checkInternet() {
        if (!isConnected()) {
            errorLiveData.value = "Internetga ulanib qayta urining"
            return
        }
    }

    override fun getUserCardDataByPan(pan: String): CardData? {
        return cardRepository.getUserCardDataByPan(pan)
    }
}