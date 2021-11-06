package uz.gita.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.model.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.databinding.ScreenSendMoneyBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.SendMoneyViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.SendMoneyViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class SendMoneyScreen : Fragment(R.layout.screen_send_money) {
    private val binding by viewBinding(ScreenSendMoneyBinding::bind)
    private val viewModel: SendMoneyViewModel by viewModels<SendMoneyViewModelImpl>()

    private var isReadyCardNumber = false
    private var isReadyMoney = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        cardNumberEditText.addTextChangedListener {
            it?.let {
                isReadyCardNumber = it.length == 16 && it.contains("8600")
                check()
            }
        }

        moneyAmountEditText.addTextChangedListener {
            it?.let {
                isReadyMoney = it.length > 2
            }
        }

        sendBtn.setOnClickListener {
            viewModel.sendMoney(
                MoneyRequest(
                    1,
                    cardNumberEditText.text.toString(),
                    moneyAmountEditText.text.toString().toLong()
                )
            )
        }

        viewModel.enableSendMoneyButton.observe(viewLifecycleOwner, enableSendMoneyObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.successLiveData.observe(viewLifecycleOwner, successObserver)
    }

    private val enableSendMoneyObserver = Observer<Unit> {
        binding.sendBtn.isEnabled = true
    }

    private val errorObserver = Observer<String> {
        showToast(it)
    }

    private val successObserver = Observer<String> {
        showToast(it)
    }

    private fun check() {
        binding.sendBtn.isEnabled =
            isReadyCardNumber && isReadyMoney
    }
}