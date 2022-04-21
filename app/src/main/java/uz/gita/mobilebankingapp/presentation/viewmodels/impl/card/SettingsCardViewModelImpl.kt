package uz.gita.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.ColorRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.EditCardRequest
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SettingsCardViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class SettingsCardViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val authRepository: AuthRepository
) :
    ViewModel(), SettingsCardViewModel {
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val openLoginScreenLiveData = MutableLiveData<LogoutResponse>()

    init {
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(LogoutResponse("LogoutCauseInternetError"))
        }
    }

    override fun editCard(data: EditCardRequest, cardId: Int, bgColor: Int) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.editCard(data).onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess {
                    putColor(cardId, bgColor)
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getMainCardData(): CardData? {
        return cardRepository.getMyMainCardData()
    }

    override fun changeMainCard(pan: String) {
        cardRepository.changeMainCard(pan)
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
                    closeScreenLiveData.value = Unit
                }
            }.launchIn(viewModelScope)
        }
    }
}