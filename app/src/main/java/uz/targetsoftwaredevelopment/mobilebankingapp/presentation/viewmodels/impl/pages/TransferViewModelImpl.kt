package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.pages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.pages.TransferViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class TransferViewModelImpl @Inject constructor(
    private val cardRepository: CardRepository,
    private val authRepository: AuthRepository
) : ViewModel(), TransferViewModel {

    override val enableNextButton = MutableLiveData<Unit>()
    override val errorLiveData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<String>()
    override val ownerNameLiveData = MutableLiveData<String>()
    override val openLoginScreenLiveData = MutableLiveData<Unit>()
    override val cardsListLiveData = MutableLiveData<List<CardData>>()
    override val panToTransferPageLiveData = MutableLiveData<String>()
    override val scannedCardNumberLiveData = MutableLiveData<String>()

    init {
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(Unit)
        }
        cardRepository.setPanToTransferPageListener {
            panToTransferPageLiveData.value = it
        }
        cardRepository.setScannedCardNumberListener {
            scannedCardNumberLiveData.value = it
        }
    }

    override fun getOwnerByPan(data: OwnerByPanRequest) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getOwnerByPan(data).onEach {
                it.onSuccess { data ->
                    enableNextButton.value = Unit
                    ownerNameLiveData.value = data.owner
                }
                it.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getAllCardList() {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            cardRepository.getAllCardsList().onEach {
                it?.onFailure { throwable ->
                    errorLiveData.value = throwable.message
                }
                it?.onSuccess {
                    cardsListLiveData.value = it!!.data!!
                }
            }.launchIn(viewModelScope)
        }
    }
}