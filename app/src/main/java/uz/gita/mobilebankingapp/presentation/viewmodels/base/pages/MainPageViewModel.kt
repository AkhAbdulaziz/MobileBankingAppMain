package uz.gita.mobilebankingapp.presentation.viewmodels.base.pages

import androidx.lifecycle.LiveData

interface MainPageViewModel {
    val totalSumLiveData: LiveData<String>
    val totalSumFromLocalLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>

    fun getTotalSum()

    fun getTotalSumFromLocal()

    fun getOutcomes()

    fun getAllCardList()

    var isBalanceVisible: Boolean
}