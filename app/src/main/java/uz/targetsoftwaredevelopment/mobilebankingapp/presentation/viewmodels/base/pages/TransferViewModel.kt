package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.pages

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.OwnerByPanRequest

interface TransferViewModel {
    val enableNextButton: LiveData<Unit>
    val errorLiveData: LiveData<String>
    val successLiveData: LiveData<String>
    val ownerNameLiveData: LiveData<String>
    val openLoginScreenLiveData: LiveData<Unit>
    val cardsListLiveData: LiveData<List<CardData>>
    val panToTransferPageLiveData: LiveData<String>
    val scannedCardNumberLiveData: LiveData<String>

    fun getOwnerByPan(data: OwnerByPanRequest)
    fun getAllCardList()
}