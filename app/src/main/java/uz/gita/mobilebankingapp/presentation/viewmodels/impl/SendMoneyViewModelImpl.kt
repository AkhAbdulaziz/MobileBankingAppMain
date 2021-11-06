package uz.gita.mobilebankingapp.presentation.viewmodels.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.model.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.SendMoneyViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModelImpl @Inject constructor(private val repository: CardRepository) :
    ViewModel(), SendMoneyViewModel {
    override val enableSendMoneyButton = MutableLiveData<Unit>()
    override val errorLiveData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<String>()

    override fun sendMoney(data: MoneyRequest) {
        if (!isConnected()) {
            errorLiveData.value = "Internetga ulanib qayta urining"
            return
        }

        repository.sendMoney(data).onEach {
            enableSendMoneyButton.value = Unit
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess { message ->
                successLiveData.value = message
            }
        }.launchIn(viewModelScope)
    }

}