package uz.gita.mobilebankingapp.presentation.ui.screens.card

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
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.databinding.ScreenSendMoneyBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SendMoneyViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.SendMoneyViewModelImpl
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class SendMoneyScreen : Fragment(R.layout.screen_send_money) {
    private val binding by viewBinding(ScreenSendMoneyBinding::bind)
    private val viewModel: SendMoneyViewModel by viewModels<SendMoneyViewModelImpl>()
    private val args: SendMoneyScreenArgs by navArgs()
    private var fullAmount: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        cardNumberText.text =
            "${args.cardNumber.substring(0, 6)}******${args.cardNumber.substring(12)}"
        moneyAmountText.text = "${args.amount} sum"
        fullAmount = "${args.amount}"

        val currentCardData: CardData? = viewModel.getMyCurrentCardData()
        if (currentCardData != null) {
            viewModel.getFee(
                MoneyRequest(
                    currentCardData.id!!,
                    args.cardNumber,
                    args.amount
                )
            )

            senderName.text = currentCardData.cardName
            senderBalance.text = "Balance: ${currentCardData.balance} so'm"
            senderCardNumber.text = "**** ${currentCardData.pan!!.substring(12)}"

            if (currentCardData.balance!! < args.amount) {
                sendBtn.isEnabled = false
                showErrorNoMoney()
            } else {
                hideErrors()
                sendBtn.isEnabled = true
            }
        } else {
            sendBtn.isEnabled = false
            showErrorNoCard()
        }

        /* val firstName = "${args.receiverName.substring(0, args.receiverName.indexOf(" "))}"
          var lastName = ""
          for (i in 0 until (args.receiverName.length - firstName.length - 1)) {
              lastName += "*"
          }
          receiverNameText.text = "$firstName $lastName"*/


        sendBtn.setOnClickListener {
            findNavController().navigate(
                SendMoneyScreenDirections.actionSendMoneyScreen2ToWaitingMoneySendScreen(
                    MoneyRequest(
                        currentCardData!!.id!!,
                        args.cardNumber,
                        args.amount
                    )
                )
            )
        }

        viewModel.enableSendMoneyButton.observe(viewLifecycleOwner, enableSendMoneyObserver)
        viewModel.feeLiveData.observe(viewLifecycleOwner, feeObserver)
    }

    private val enableSendMoneyObserver = Observer<Unit> {
        binding.sendBtn.isEnabled = true
    }

    private val feeObserver = Observer<String> {
        binding.feeText.text = "$it so'm"
        binding.fullAmountText.text = "${fullAmount + it.toLong()} so'm"
    }

    private fun showErrorNoCard() = binding.scope {
        errorImg.visible()
        errorText.text = "You have not a card!"
        errorText.visible()
    }

    private fun showErrorNoMoney() = binding.scope {
        errorImg.visible()
        errorText.text = "You have not enough money!"
        errorText.visible()
    }

    private fun hideErrors() = binding.scope {
        errorImg.gone()
        errorText.gone()
    }
}