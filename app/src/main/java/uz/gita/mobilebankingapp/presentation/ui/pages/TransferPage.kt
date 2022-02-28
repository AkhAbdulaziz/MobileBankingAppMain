package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.RecipientData
import uz.gita.mobilebankingapp.data.enums.PaymentPageEnum
import uz.gita.mobilebankingapp.databinding.PageTransferBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.transferPageAdapters.RecipientsAdapter
import uz.gita.mobilebankingapp.presentation.ui.screens.main.BasicScreenDirections
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.PaymentViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.PaymentViewModelImpl
import uz.gita.mobilebankingapp.utils.invisible
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class TransferPage : Fragment(R.layout.page_transfer) {
    private val binding by viewBinding(PageTransferBinding::bind)
    private val viewModel: PaymentViewModel by viewModels<PaymentViewModelImpl>()
    private var recipientsList = ArrayList<RecipientData>()
    private val recipientsAdapter by lazy { RecipientsAdapter(recipientsList) }
    private var isFirstTime: Boolean = true

    private var isReadyCardNumber = false
    private var isReadyMoney = false
    private var receiverName: String = ""
    private val directionType get() = arguments?.getString("direction_type")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        if (directionType != null && directionType == PaymentPageEnum.FROM_BASE_SCREEN.name) {
            backBtn.invisible()
        } else {
            backBtn.visible()
        }

        if (isFirstTime) {
            fillRecipientsList()
            isFirstTime = false
        }

        rvRecipients.adapter = recipientsAdapter
        rvRecipients.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recipientsAdapter.setExistRecipientClickListener {

        }
        recipientsAdapter.setNewRecipientClickListener {

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
            if (directionType != null && directionType == PaymentPageEnum.FROM_BASE_SCREEN.name) {
                findNavController().navigate(
                    BasicScreenDirections.actionBasicScreenToSendMoneyScreen(
                        cardNumberEditText.text.toString(),
                        moneyAmountEditText.text.toString().toLong(),
                        receiverName
                    )
                )
            } else {
                findNavController().navigate(
                    TransferPageDirections.actionPaymentPageToSendMoneyScreen(
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

    private fun fillRecipientsList() {
        recipientsList.apply {
            add(
                RecipientData(
                    "SP",
                    "Steven Paul Jobs",
                    R.color.colorRed
                )
            )
            add(
                RecipientData(
                    "WB",
                    "William Bradley Pitt",
                    R.color.yellow
                )
            )
            add(
                RecipientData(
                    "WH",
                    "William Henry Gates",
                    R.color.colorGreen
                )
            )
            add(
                RecipientData(
                    "ME",
                    "Mark Elliot Zuckerberg",
                    R.color.orange
                )
            )
            add(
                RecipientData(
                    "SM",
                    "Sergey Mikhaylovich Brin",
                    R.color.purple_500
                )
            )
            add(
                RecipientData(
                    "LE",
                    "Lawrence Edward Page",
                    R.color.brown
                )
            )
            add(
                RecipientData(
                    "PS",
                    "Pichai Sundararajan",
                    R.color.orange
                )
            )
            add(
                RecipientData(
                    "",
                    "Add Recipient",
                    R.color.orange,
                    true
                )
            )
        }
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