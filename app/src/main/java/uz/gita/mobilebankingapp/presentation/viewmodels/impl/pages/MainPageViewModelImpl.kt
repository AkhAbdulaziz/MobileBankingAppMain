package uz.gita.mobilebankingapp.presentation.viewmodels.impl.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.pages.MainPageViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class MainPageViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel(), MainPageViewModel {

    override val totalSumLiveData = MutableLiveData<String>()
    override val totalSumFromLocalLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val cardsListLiveData = MutableLiveData<List<CardData>>()

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
                it.onSuccess { data ->
                    totalSumLiveData.value = "${data.data}"
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getOutcomes() {

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

    override var isBalanceVisible: Boolean
        get() = cardRepository.isBalanceVisible
        set(visibility) {
            cardRepository.isBalanceVisible = visibility
        }
}