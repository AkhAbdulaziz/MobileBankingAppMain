package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.DeleteCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.MyCardsViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class MyCardsViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val authRepository: AuthRepository
) : ViewModel(), MyCardsViewModel {

    override val cardsListLiveData = MutableLiveData<List<CardData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val closeDialogLiveData = MutableLiveData<Unit>()
    override val openLoginScreenLiveData = MutableLiveData<Unit>()

    init {
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(Unit)
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

    override fun deleteCard(data: DeleteCardRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.deleteCard(data).onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess {
                    closeDialogLiveData.value = Unit
                }
            }.launchIn(viewModelScope)
        }
    }
}