package uz.gita.mobilebankingapp.presentation.ui.pages

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
import uz.gita.mobilebankingapp.data.enums.PaymentPageEnum
import uz.gita.mobilebankingapp.databinding.PagePaymentBinding
import uz.gita.mobilebankingapp.presentation.ui.screens.main.BasicScreenDirections
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.PaymentViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.PaymentViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class PaymentPage : Fragment(R.layout.page_payment) {
    private val binding by viewBinding(PagePaymentBinding::bind)
    private val viewModel: PaymentViewModel by viewModels<PaymentViewModelImpl>()

    private var isReadyCardNumber = false
    private var isReadyMoney = false
    private var receiverName: String = ""
    private var directionType = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            directionType = it.getString("direction_type")!!
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        cardNumberEditText.addTextChangedListener {
            it?.let {
                isReadyCardNumber = it.toString().length == 16 && it.contains("8600")
                check()
            }
            if (isReadyCardNumber) {
//                 Shu joyida crash beryatgandi

                /* viewModel.getOwnerByPan(
                     OwnerByPanRequest(
                         it.toString()
                     )
                 )*/
            }
        }

        moneyAmountEditText.addTextChangedListener {
            it?.let {
                isReadyMoney = if (it.toString().isEmpty()) {
                    false
                } else {
                    (it.toString()).toLong() >= 500
                }
                check()
            }
        }

        nextBtn.setOnClickListener {
            if (directionType == PaymentPageEnum.FROM_BASE_SCREEN.name) {
                findNavController().navigate(
                    BasicScreenDirections.actionBasicScreenToSendMoneyScreen(
                        cardNumberEditText.text.toString(),
                        moneyAmountEditText.text.toString().toLong(),
                        receiverName
                    )
                )
            } else {
                findNavController().navigate(
                    PaymentPageDirections.actionPaymentPageToSendMoneyScreen(
                        cardNumberEditText.text.toString(),
                        moneyAmountEditText.text.toString().toLong(),
                        receiverName
                    )
                )
            }
        }

        viewModel.enableNextButton.observe(viewLifecycleOwner, enableNextObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.successLiveData.observe(viewLifecycleOwner, successObserver)
        viewModel.ownerNameLiveData.observe(viewLifecycleOwner, ownerNameObserver)
    }

    private val enableNextObserver = Observer<Unit> {
        binding.nextBtn.isEnabled = true
    }

    private val errorObserver = Observer<String> {
        showToast(it)
    }

    private val successObserver = Observer<String> {
        showToast(it)
    }

    private val ownerNameObserver = Observer<String> { ownerName ->
        receiverName = ownerName
        binding.cardOwnerNameText.apply {
            text = "Kartaning egasi: $ownerName"
            visible()
        }
    }

    private fun check() {
        binding.nextBtn.isEnabled =
            isReadyCardNumber && isReadyMoney
    }
}