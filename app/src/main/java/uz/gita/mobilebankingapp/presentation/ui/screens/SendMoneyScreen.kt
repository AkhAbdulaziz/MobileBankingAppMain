package uz.gita.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.CardData
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
    private val args: SendMoneyScreenArgs by navArgs()
    private var fullAmount: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        val currentCardData: CardData = viewModel.getUserCardDataByPan(args.cardNumber)!!

        cardNumberText.text =
            "${args.cardNumber.substring(0, 6)}******${args.cardNumber.substring(12)}"

        val firstName = "${args.receiverName.substring(0, args.receiverName.indexOf(" "))}"
        var lastName = ""
        for (i in 0 until (args.receiverName.length - firstName.length - 1)) {
            lastName += "*"
        }
        receiverNameText.text = "$firstName $lastName"

        moneyAmountText.text = "${args.amount} sum"
        viewModel.getFee(
            MoneyRequest(
                currentCardData.id!!,
                args.cardNumber,
                args.amount
            )
        )
        fullAmount = "${args.amount}"

        senderName.text = currentCardData.cardName
        senderBalance.text = "${currentCardData.balance} so'm"
        senderCardNumber.text = "${currentCardData.pan}"

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        sendBtn.setOnClickListener {
            viewModel.sendMoney(
                MoneyRequest(
                    currentCardData.id,
                    args.cardNumber,
                    args.amount
                )
            )
        }

        viewModel.enableSendMoneyButton.observe(viewLifecycleOwner, enableSendMoneyObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.successLiveData.observe(viewLifecycleOwner, successObserver)
        viewModel.feeLiveData.observe(viewLifecycleOwner, feeObserver)
    }

    private val enableSendMoneyObserver = Observer<Unit> {
        binding.sendBtn.isEnabled = true
    }

    private val errorObserver = Observer<String> {
        showToast(it)
    }

    private val successObserver = Observer<String> {
        findNavController().popBackStack()
        showToast(it)
    }

    private val feeObserver = Observer<String> {
        binding.feeText.text = "$it so'm"
        binding.fullAmountText.text = "${fullAmount + it.toLong()} so'm"
    }
}