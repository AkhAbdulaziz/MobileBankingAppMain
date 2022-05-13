package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.pages

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData

interface MainPageViewModel {
    val totalSumLiveData: LiveData<String>
    val totalSumFromLocalLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>
    val cardsListLiveData: LiveData<List<CardData>>
    val expendituresLiveData: LiveData<String>

    fun getTotalSum()

    fun getTotalSumFromLocal()

    fun getAllCardList()

    fun getHistoryPagingData()

    var isBalanceVisible: Boolean
}