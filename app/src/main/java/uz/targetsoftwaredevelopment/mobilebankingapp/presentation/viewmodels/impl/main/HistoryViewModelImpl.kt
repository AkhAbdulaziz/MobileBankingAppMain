package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.OwnerByIdRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.PanByIdRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.HistoryRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.HistoryViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class HistoryViewModelImpl @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val cardRepository: CardRepository
) : ViewModel(), HistoryViewModel {

    override val historyDataCountLiveData = MutableLiveData<Int>()
    override val argumentDataLiveData = MutableLiveData<SavedPaymentData>()
    override val incomesLiveData = MutableLiveData<String>()
    override val expendituresLiveData = MutableLiveData<String>()

    init {
        cardRepository.setDataToHistoryPageListener {
            argumentDataLiveData.value = it
        }
    }

    override val senderPanByIdLiveData = MutableLiveData<String>()
    override val receiverPanByIdLiveData = MutableLiveData<String>()
    override val errorLiveData = MutableLiveData<String>()
    override val ownerNameLiveData = MutableLiveData<String>()
    override val historyPagingLiveData =
        MutableLiveData<PagingData<MoneyTransferResponse.HistoryData>>()

    override fun getSenderPanById(data: PanByIdRequest) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getPanById(data).onEach {
                it.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
                it.onSuccess { data ->
                    senderPanByIdLiveData.value = data.data!!
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getReceiverPanById(data: PanByIdRequest) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getPanById(data).onEach {
                it.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
                it.onSuccess { data ->
                    receiverPanByIdLiveData.value = data.data!!
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getOwnerById(data: OwnerByIdRequest) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getOwnerById(data).onEach {
                it.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
                it.onSuccess { data ->
                    ownerNameLiveData.value = data.data!!.fio!!
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getHistoryPagingData() {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            historyRepository.getHistoryPagingData(viewModelScope).onEach {
                historyDataCountLiveData.value = historyRepository.getHistoryDataCount()
                incomesLiveData.value = historyRepository.getIncomes()
                expendituresLiveData.value = historyRepository.getExpenditures()
                historyPagingLiveData.value = it
            }.launchIn(viewModelScope)
        }
    }
}