package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.CheckData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.OwnerByIdRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.PanByIdRequest
import uz.gita.mobilebankingapp.databinding.PageHistoryBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.HistoryAdapter
import uz.gita.mobilebankingapp.presentation.ui.adapter.PassengersLoadStateAdapter
import uz.gita.mobilebankingapp.presentation.ui.screens.main.BasicScreenDirections
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.HistoryViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.HistoryViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class HistoryPage : Fragment(R.layout.page_history) {
    private val binding by viewBinding(PageHistoryBinding::bind)
    private val viewModel: HistoryViewModel by viewModels<HistoryViewModelImpl>()
    private val adapter by lazy { HistoryAdapter() }
    private var receiverName = "Johnni Receiver"
    private var receiverPan = "86001122"
    private var senderPan = "86003344"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        refresh.isRefreshing = true

        refresh.setOnRefreshListener {
            viewModel.getHistoryPagingData().onEach {
                adapter.submitData(it)
                refresh.isRefreshing = true
            }.launchIn(lifecycleScope)
        }

        historyList.adapter = adapter
        adapter.withLoadStateHeaderAndFooter(
            PassengersLoadStateAdapter { adapter.retry() },
            PassengersLoadStateAdapter { adapter.retry() }
        )

        adapter.setItemClickListener { historyData ->
            // TODO: getSenderPanById, getReceiverPanById, getOwnerById lar ishlamayapti
           /* viewModel.getSenderPanById(PanByIdRequest((historyData.sender)))
            viewModel.getReceiverPanById(PanByIdRequest(historyData.receiver))
            viewModel.getOwnerById(OwnerByIdRequest(historyData.receiver))*/

            findNavController().navigate(
                BasicScreenDirections.actionBasicScreenToCheckTransferScreen(
                    CheckData(
                        receiverPan,
                        receiverName,
                        historyData.time,
                        senderPan,
                        historyData.fee,
                        (historyData.amount + historyData.fee)
                    )
                )
            )
        }

        historyList.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getHistoryPagingData().onEach {
            adapter.submitData(it)
            binding.refresh.isRefreshing = false
        }.launchIn(lifecycleScope)

        viewModel.senderPanByIdLiveData.observe(viewLifecycleOwner, senderPanByIdObserver)
        viewModel.receiverPanByIdLiveData.observe(viewLifecycleOwner, receiverPanByIdObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.ownerNameLiveData.observe(viewLifecycleOwner, ownerNameObserver)
    }

    private val senderPanByIdObserver = Observer<String> { pan ->
        senderPan = pan
    }

    private val receiverPanByIdObserver = Observer<String> { pan ->
        receiverPan = pan
    }

    private val ownerNameObserver = Observer<String> { name ->
        receiverName = name
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }
}