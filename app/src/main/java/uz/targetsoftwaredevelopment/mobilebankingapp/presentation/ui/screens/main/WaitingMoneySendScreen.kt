package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.response.ReceiptMoneyData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenWaitingMoneySendBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.WaitingMoneySendViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main.WaitingMoneySendViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast

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
        showFancyToast(
            it,
            FancyToast.LENGTH_SHORT,
            FancyToast.ERROR
        )
    }

    private val successObserver = Observer<ReceiptMoneyData> {
        findNavController().navigate(WaitingMoneySendScreenDirections.actionWaitingMoneySendScreenToSucceedMoneySendScreen())
    }
}