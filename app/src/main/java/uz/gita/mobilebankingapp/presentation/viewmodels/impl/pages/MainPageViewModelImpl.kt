package uz.gita.mobilebankingapp.presentation.viewmodels.impl.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.pages.MainPageViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class MainPageViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val appRepository: AppRepository
) :
    ViewModel(), MainPageViewModel {
    override val totalSumLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun getTotalSumFromLocal(): String {
        return cardRepository.getTotalSumFromLocal()
    }

    override fun getTotalSum() {
        if (!isConnected()) {
            errorMessageLiveData.value = "Internet mavjud emas"
            return
        }

        cardRepository.getTotalSum().onEach {
            it.onFailure { throwable ->
                errorMessageLiveData.value = throwable.message
            }
            it.onSuccess { data ->
                totalSumLiveData.value = "${data.data}"
            }
        }.launchIn(viewModelScope)
    }

    override fun getOutcomes() {
        TODO("Not yet implemented")
    }

    override fun getAllCardList() {
        cardRepository.getAllCardsList().onEach {
            it?.onFailure { throwable ->
                errorMessageLiveData.value = throwable.message
            }
            it?.onSuccess {

            }
        }.launchIn(viewModelScope)
    }

    override var isBalanceVisible: Boolean
        get() = cardRepository.isBalanceVisible
        set(visibility) {
            cardRepository.isBalanceVisible = visibility
        }
}