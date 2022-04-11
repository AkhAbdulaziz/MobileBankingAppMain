package uz.gita.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.AddCardRequest
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.AddCardViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class AddCardViewModelImpl @Inject constructor(
    private val repository: CardRepository,
    private val authRepository: AuthRepository
) : ViewModel(), AddCardViewModel {
    override val enableAddButtonLiveData = MutableLiveData<Unit>()
    override val disableAddButtonLiveData = MutableLiveData<Unit>()
    override val openVerifyCardScreenLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val openLoginScreenLiveData = MutableLiveData<Unit>()

    init {
        repository.setCardVerifiedListener {
            closeScreenLiveData.postValue(Unit)
        }
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(Unit)
        }
    }

    override fun addCard(cardData: AddCardRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            repository.addCard(cardData).onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess {
                    openVerifyCardScreenLiveData.value = Unit
                    enableAddButtonLiveData.value = Unit
                }
            }.launchIn(viewModelScope)
        }
    }
}