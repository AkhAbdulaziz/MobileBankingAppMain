package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByIdRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.PanByIdRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.domain.repository.HistoryRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.HistoryViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class HistoryViewModelImpl @Inject constructor(
    private val repository: HistoryRepository,
    private val cardRepository: CardRepository
) : ViewModel(), HistoryViewModel {
    override val senderPanByIdLiveData = MutableLiveData<String>()
    override val receiverPanByIdLiveData = MutableLiveData<String>()
    override val errorLiveData = MutableLiveData<String>()
    override val ownerNameLiveData = MutableLiveData<String>()

    override fun getSenderPanById(data: PanByIdRequest) {
        checkInternet()
        cardRepository.getPanById(data).onEach {
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess { data ->
                senderPanByIdLiveData.value = data.data
            }
        }.launchIn(viewModelScope)
    }

    override fun getReceiverPanById(data: PanByIdRequest) {
        checkInternet()
        cardRepository.getPanById(data).onEach {
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess { data ->
                receiverPanByIdLiveData.value = data.data
            }
        }.launchIn(viewModelScope)
    }

    override fun getOwnerById(data: OwnerByIdRequest) {
        checkInternet()

        cardRepository.getOwnerById(data).onEach {
            it.onFailure { throwable ->
                errorLiveData.value = throwable.message
            }
            it.onSuccess { data ->
                ownerNameLiveData.value = data.data!!.fio
            }
        }.launchIn(viewModelScope)
    }

    override fun getHistoryPagingData(): Flow<PagingData<MoneyTransferResponse.HistoryData>> =
        repository.getHistoryPagingData(viewModelScope)

    private fun checkInternet() {
        if (!isConnected()) {
            errorLiveData.value = "Internetga ulanib qayta urining"
            return
        }
    }
}