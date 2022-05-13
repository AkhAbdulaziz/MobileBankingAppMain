package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.SavedPaymentViewModel
import javax.inject.Inject

@HiltViewModel
class SavedPaymentViewModelImpl @Inject constructor(private val cardRepository: CardRepository) :
    ViewModel(), SavedPaymentViewModel {

    override fun giveDataToHistoryPage(data: SavedPaymentData) {
        cardRepository.giveDataToHistoryPage(data)
    }
}