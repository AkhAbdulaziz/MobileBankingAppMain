package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main

import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData

interface SavedPaymentViewModel {
    fun giveDataToHistoryPage(data: SavedPaymentData)
}