package uz.gita.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.DeleteCardRequest
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.MyCardsViewModel
import uz.gita.mobilebankingapp.utils.sortMainCard
import javax.inject.Inject

@HiltViewModel
class MyCardsViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val appRepository: AppRepository
) :
    ViewModel(), MyCardsViewModel {

    override val cardsListLiveData = MutableLiveData<List<CardData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val closeDialogLiveData = MutableLiveData<Unit>()

    override fun getAllCardList() {
        cardRepository.getAllCardsList().onEach {
            it?.onFailure { throwable ->
                errorMessageLiveData.value = throwable.message
            }
            it?.onSuccess {
                cardsListLiveData.value = it!!.data!!
            }
        }.launchIn(viewModelScope)
    }

    override fun deleteCard(data: DeleteCardRequest) {
        cardRepository.deleteCard(data).onEach {
            it?.onFailure { throwable ->
                errorMessageLiveData.value = throwable.message
            }
            it?.onSuccess {
                closeDialogLiveData.value = Unit
            }
        }.launchIn(viewModelScope)
    }
}