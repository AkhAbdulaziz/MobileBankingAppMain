package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.ColorRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.VerifyCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.ResendRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.VerifyCardViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class VerifyCardViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val cardRepository: CardRepository
) : ViewModel(), VerifyCardViewModel {

    override val userPhoneNumberLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val exitScreenLiveData = MutableLiveData<Unit>()
    override val resendCodeLiveData = MutableLiveData<Unit>()
    override val userPasswordLiveData = MutableLiveData<String>()
    override val currentPanLiveData = MutableLiveData<String>()

    override fun verifyCard(data: VerifyCardRequest, bgColor: Int) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.verifyCard(data).onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess { newCardData ->
                    putColor(newCardData.id, bgColor)
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun resendCode(data: ResendRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.resend(data).onEach {
                it.onSuccess {
                    resendCodeLiveData.value = Unit
                }
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun putColor(cardId: Int, bgColor: Int) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.putColor(ColorRequest(cardId, bgColor)).onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess {
                    exitScreenLiveData.value = Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getCurrentPan() {
        cardRepository.getCurrentPan().onEach {
            it.onSuccess {
                currentPanLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun getUserPhoneNumber() {
        authRepository.getUserPhoneNumber().onEach {
            it.onSuccess {
                userPhoneNumberLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun getUserPassword() {
        userPasswordLiveData.value = authRepository.getUserPassword()
    }
}