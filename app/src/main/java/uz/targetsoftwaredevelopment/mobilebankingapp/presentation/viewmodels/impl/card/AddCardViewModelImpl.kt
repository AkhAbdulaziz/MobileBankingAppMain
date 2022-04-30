package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.AddCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.ColorRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.AddCardViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class AddCardViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val authRepository: AuthRepository
) : ViewModel(), AddCardViewModel {
    override val openVerifyCardScreenLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val openLoginScreenLiveData = MutableLiveData<Unit>()

    init {
        cardRepository.setCardVerifiedListener {
            closeScreenLiveData.postValue(Unit)
        }
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(Unit)
        }
    }

    override fun addCard(cardData: AddCardRequest, bgColor: Int) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.addCard(cardData).onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess {
                    putColor(it.id, bgColor)
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
                    closeScreenLiveData.value = Unit
                }
            }.launchIn(viewModelScope)
        }
    }
}