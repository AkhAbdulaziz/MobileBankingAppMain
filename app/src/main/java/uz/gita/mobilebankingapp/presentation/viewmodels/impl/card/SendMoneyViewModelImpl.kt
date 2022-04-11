package uz.gita.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.ReceiptMoneyData
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SendMoneyViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel(), SendMoneyViewModel {

    override val enableSendMoneyButton = MutableLiveData<Unit>()
    override val errorLiveData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<ReceiptMoneyData>()
    override val ownerNameLiveData = MutableLiveData<String>()
    override val feeLiveData = MutableLiveData<String>()

    override fun getOwnerByPan(data: OwnerByPanRequest) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
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
    }

    override fun sendMoney(data: MoneyRequest) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.sendMoney(data).onEach {
                it.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
                it.onSuccess { data ->
                    successLiveData.value = data
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getFee(data: MoneyRequest) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getFee(data).onEach {
                it.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
                it.onSuccess {
                    feeLiveData.value = it.message
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getMyMainCardData(): CardData? {
        return cardRepository.getMyMainCardData()
    }
}