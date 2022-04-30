package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.ReceiptMoneyData
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.SendMoneyViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
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
    override val cardsListLiveData = MutableLiveData<List<CardData>>()

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

    override fun getAllCardList() {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getAllCardsList().onEach {
                it?.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
                it?.onSuccess {
                    cardsListLiveData.value = it!!.data!!
                }
            }.launchIn(viewModelScope)
        }
    }
}