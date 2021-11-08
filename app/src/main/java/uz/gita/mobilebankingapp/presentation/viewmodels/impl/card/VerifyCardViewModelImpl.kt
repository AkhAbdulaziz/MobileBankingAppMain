package uz.gita.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.VerifyCardRequest
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.VerifyCardViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class VerifyCardViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val cardRepository: CardRepository
) :
    ViewModel(), VerifyCardViewModel {
    override val errorMessageLiveData = MutableLiveData<String>()
    override val openMyCardsScreenLiveData = MutableLiveData<Unit>()

    override fun verifyCard(data: VerifyCardRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "Internet mavjud emas"
            return
        }

        cardRepository.verifyCard(data).onEach {
            it.onFailure { throwable ->
                errorMessageLiveData.value = throwable.message
            }
            it.onSuccess {
                openMyCardsScreenLiveData.value = Unit
            }
        }.launchIn(viewModelScope)
    }

    override fun getCurrentPan(): String {
        return cardRepository.getCurrentPan()
    }

    override fun getUserPhoneNumber(): String {
        return authRepository.getUserPhoneNumber()
    }
}