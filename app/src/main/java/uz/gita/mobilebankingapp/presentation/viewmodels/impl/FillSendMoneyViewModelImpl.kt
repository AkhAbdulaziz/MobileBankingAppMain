package uz.gita.mobilebankingapp.presentation.viewmodels.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.model.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.data.model.card_req_res.request.OwnerByPanRequest
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.FillSendMoneyViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.SendMoneyViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class FillSendMoneyViewModelImpl @Inject constructor(private val repository: CardRepository) :
    ViewModel(), FillSendMoneyViewModel {
    override val enableNextButton = MutableLiveData<Unit>()
    override val errorLiveData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<String>()
    override val ownerNameLiveData = MutableLiveData<String>()

    override fun getOwnerByPan(data: OwnerByPanRequest) {
        checkInternet()
        repository.getOwnerByPan(data).onEach {
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess { data ->
                enableNextButton.value = Unit
                ownerNameLiveData.value = data.owner
            }
        }.launchIn(viewModelScope)
    }

    private fun checkInternet() {
        if (!isConnected()) {
            errorLiveData.value = "Internetga ulanib qayta urining"
            return
        }
    }
}