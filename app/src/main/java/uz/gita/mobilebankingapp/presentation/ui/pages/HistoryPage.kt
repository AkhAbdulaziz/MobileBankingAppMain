package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.PageHistoryBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.HistoryAdapter
import uz.gita.mobilebankingapp.presentation.ui.adapter.PassengersLoadStateAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.HistoryViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.HistoryViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class HistoryPage : Fragment(R.layout.page_history) {
    private val binding by viewBinding(PageHistoryBinding::bind)
    private val viewModel: HistoryViewModel by viewModels<HistoryViewModelImpl>()
    private val adapter by lazy { HistoryAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        historyList.adapter = adapter
        adapter.withLoadStateHeaderAndFooter(
            PassengersLoadStateAdapter { adapter.retry() },
            PassengersLoadStateAdapter { adapter.retry() }
        )
        showToast("open HistoryPage")
        historyList.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getHistoryPagingData().onEach {
            adapter.submitData(it)
        }.launchIn(lifecycleScope)
    }
}