package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.HistoryRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.pages.MainPageViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class MainPageViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val historyRepository: HistoryRepository
) : ViewModel(), MainPageViewModel {

    override val totalSumLiveData = MutableLiveData<String>()
    override val totalSumFromLocalLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val cardsListLiveData = MutableLiveData<List<CardData>>()
    override val expendituresLiveData = MutableLiveData<String>()

    override fun getTotalSumFromLocal() {
        cardRepository.getTotalSumFromLocal().onEach {
            it.onSuccess {
                totalSumFromLocalLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun getTotalSum() {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getTotalSum().onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess { amount ->
                    totalSumLiveData.value = amount
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getAllCardList() {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getAllCardsList().onEach {
                it?.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it?.onSuccess {
                    cardsListLiveData.value = it!!.data!!
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getHistoryPagingData() {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            historyRepository.getHistoryPagingData(viewModelScope).onEach {
                expendituresLiveData.value = historyRepository.getExpenditures()
            }.launchIn(viewModelScope)
        }
    }

    override var isBalanceVisible: Boolean
        get() = cardRepository.isBalanceVisible
        set(visibility) {
            cardRepository.isBalanceVisible = visibility
        }
}