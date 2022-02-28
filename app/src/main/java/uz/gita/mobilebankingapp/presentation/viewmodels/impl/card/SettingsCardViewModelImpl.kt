package uz.gita.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.ColorRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.EditCardRequest
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SettingsCardViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class SettingsCardViewModelImpl @Inject constructor(private val cardRepository: CardRepository) :
    ViewModel(), SettingsCardViewModel {
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun editCard(data: EditCardRequest, cardId: Int, bgColor: Int) {
        checkInternet()

        cardRepository.editCard(data).onEach {
            it.onFailure { throwable ->
                errorMessageLiveData.value = throwable.message
            }
            it.onSuccess {
                putColor(cardId, bgColor)
            }
        }.launchIn(viewModelScope)
    }

    private fun checkInternet() {
        if (!isConnected()) {
            errorMessageLiveData.value = "Internet mavjud emas"
            return
        }
    }

    override fun getMainCardData(): CardData? {
        return cardRepository.getMyMainCardData()
    }

    override fun changeMainCard(pan: String) {
        cardRepository.changeMainCard(pan)
    }

    private fun putColor(cardId: Int, bgColor: Int) {
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