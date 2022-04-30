package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.ReceiptMoneyData
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.WaitingMoneySendViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class WaitingMoneySendViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel(), WaitingMoneySendViewModel {
    override val errorLiveData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<ReceiptMoneyData>()

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
}