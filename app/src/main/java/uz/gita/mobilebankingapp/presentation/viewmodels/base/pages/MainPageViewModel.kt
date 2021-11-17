package uz.gita.mobilebankingapp.presentation.viewmodels.base.pages

import androidx.lifecycle.LiveData

interface MainPageViewModel {
    val totalSumLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>

    fun getTotalSum()

    fun getTotalSumFromLocal(): String

    fun getOutcomes()

    fun getAllCardList()

    var isBalanceVisible: Boolean
}