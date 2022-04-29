package uz.gita.mobilebankingapp.presentation.viewmodels.base.pages

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData

interface MainPageViewModel {
    val totalSumLiveData: LiveData<String>
    val totalSumFromLocalLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>
    val cardsListLiveData: LiveData<List<CardData>>

    fun getTotalSum()

    fun getTotalSumFromLocal()

    fun getOutcomes()

    fun getAllCardList()

    var isBalanceVisible: Boolean
}