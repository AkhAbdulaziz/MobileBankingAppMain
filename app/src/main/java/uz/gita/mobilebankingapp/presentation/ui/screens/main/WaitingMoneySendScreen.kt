package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.ReceiptMoneyData
import uz.gita.mobilebankingapp.databinding.ScreenWaitingMoneySendBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.WaitingMoneySendViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.WaitingMoneySendViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class WaitingMoneySendScreen : Fragment(R.layout.screen_waiting_money_send) {
    private val binding by viewBinding(ScreenWaitingMoneySendBinding::bind)
    private val viewModel: WaitingMoneySendViewModel by viewModels<WaitingMoneySendViewModelImpl>()
    private val args: WaitingMoneySendScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        animationView.playAnimation()
        viewModel.sendMoney(args.moneyRequest)

        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.successLiveData.observe(viewLifecycleOwner, successObserver)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {

                }
            })
    }

    private val errorObserver = Observer<String> {
        showToast(it)
    }

    private val successObserver = Observer<ReceiptMoneyData> {
        findNavController().navigate(WaitingMoneySendScreenDirections.actionWaitingMoneySendScreenToSucceedMoneySendScreen())
    }
}