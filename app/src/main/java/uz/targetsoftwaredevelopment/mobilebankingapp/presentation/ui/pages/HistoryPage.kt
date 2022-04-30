package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.CheckData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.OwnerByIdRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.PanByIdRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.PageHistoryBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.HistoryAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.PassengersLoadStateAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main.BasicScreenDirections
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.HistoryViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main.HistoryViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.gone
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showToast
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible

@AndroidEntryPoint
class HistoryPage : Fragment(R.layout.page_history) {
    private val binding by viewBinding(PageHistoryBinding::bind)
    private val viewModel: HistoryViewModel by viewModels<HistoryViewModelImpl>()
    private val adapter by lazy { HistoryAdapter() }
    private var receiverName = "John Smith"
    private var receiverPan = "860010******3000"
    private var senderPan = "860012******1234"

    private var backPressedListener: ((Int) -> Unit)? = null
    fun setBackPressedListener(f: (Int) -> Unit) {
        backPressedListener = f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressedListener?.invoke(4)
                }
            })

        getHistoryData()
        refresh.setOnRefreshListener {
            getHistoryData()
        }

        historyList.adapter = adapter
        adapter.withLoadStateHeaderAndFooter(
            PassengersLoadStateAdapter { adapter.retry() },
            PassengersLoadStateAdapter { adapter.retry() }
        )

        adapter.setItemClickListener { historyData ->
//             TODO: getSenderPanById, getReceiverPanById, getOwnerById lar ishlamayapti
            viewModel.getSenderPanById(PanByIdRequest((historyData.sender)))
            viewModel.getReceiverPanById(PanByIdRequest(historyData.receiver))
            viewModel.getOwnerById(OwnerByIdRequest(historyData.receiver))

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

        viewModel.senderPanByIdLiveData.observe(viewLifecycleOwner, senderPanByIdObserver)
        viewModel.receiverPanByIdLiveData.observe(viewLifecycleOwner, receiverPanByIdObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.ownerNameLiveData.observe(viewLifecycleOwner, ownerNameObserver)
        viewModel.historyPagingLiveData.observe(viewLifecycleOwner, historyPagingDataObserver)
        viewModel.historyDataCountLiveData.observe(viewLifecycleOwner, historyDataCountObserver)
    }

    private fun getHistoryData() = binding.scope {
        refresh.isRefreshing = true
        viewModel.getHistoryPagingData()
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

    private val historyPagingDataObserver =
        Observer<PagingData<MoneyTransferResponse.HistoryData>> {
            viewModel.getHistoryDataCount()
            CoroutineScope(Dispatchers.IO).launch {
                adapter.submitData(it)
            }
            binding.refresh.isRefreshing = false
        }

    private val historyDataCountObserver = Observer<Int> { dataCount ->
        binding.apply {
            if (dataCount == 0) {
                txtEmpty.visible()
                imgEmpty.visible()
            } else {
                txtEmpty.gone()
                imgEmpty.gone()
            }
        }
    }
}