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
import uz.gita.mobilebankingapp.data.model.card_req_res.request.OwnerByPanRequest
import uz.gita.mobilebankingapp.databinding.ScreenFillSendMoneyBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.FillSendMoneyViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.FillSendMoneyViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class FillSendMoneyScreen : Fragment(R.layout.screen_fill_send_money) {
    private val binding by viewBinding(ScreenFillSendMoneyBinding::bind)
    private val viewModel: FillSendMoneyViewModel by viewModels<FillSendMoneyViewModelImpl>()

    private var isReadyCardNumber = false
    private var isReadyMoney = false
    private var receiverName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        cardNumberEditText.addTextChangedListener {
            it?.let {
                isReadyCardNumber = it.toString().length == 16 && it.contains("8600")
                check()
            }
            if (isReadyCardNumber) {
                viewModel.getOwnerByPan(
                    OwnerByPanRequest(
                        it.toString()
                    )
                )
            }
        }

        moneyAmountEditText.addTextChangedListener {
            it?.let {
                isReadyMoney = it.toString().length > 2
                check()
            }
        }

        nextBtn.setOnClickListener {
            findNavController().navigate(
                FillSendMoneyScreenDirections.actionFillSendMoneyScreenToSendMoneyScreen2(
                    cardNumberEditText.text.toString(),
                    moneyAmountEditText.text.toString().toLong(),
                    receiverName
                )
            )
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
        findNavController().popBackStack()
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