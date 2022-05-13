package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.ScanCardViewModel
import javax.inject.Inject

@HiltViewModel
class ScanCardViewModelImpl @Inject constructor(private val cardRepository: CardRepository) :
    ScanCardViewModel, ViewModel() {

    override fun saveScannedCardNumber(cardNumber: String) {
        cardRepository.saveScannedCardNumber(cardNumber)
    }
}