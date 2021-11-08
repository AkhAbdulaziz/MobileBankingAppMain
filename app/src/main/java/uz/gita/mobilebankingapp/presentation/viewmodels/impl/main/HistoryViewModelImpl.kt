package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.gita.mobilebankingapp.domain.repository.HistoryRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.HistoryViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModelImpl @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel(), HistoryViewModel {

    override fun getHistoryPagingData(): Flow<PagingData<MoneyTransferResponse.HistoryData>> =
        repository.getHistoryPagingData(viewModelScope)
}