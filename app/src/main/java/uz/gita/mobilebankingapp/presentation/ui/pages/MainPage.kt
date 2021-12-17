package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.enum.PaymentPageEnum
import uz.gita.mobilebankingapp.databinding.PageMainBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.pages.MainPageViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.pages.MainPageViewModelImpl
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.invisible
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class MainPage : Fragment(R.layout.page_main) {
    private val binding by viewBinding(PageMainBinding::bind)
    private val viewModel: MainPageViewModel by viewModels<MainPageViewModelImpl>()

    private var clickHomeButtonListener: (() -> Unit)? = null
    fun setOnClickHomeButtonListener(block: () -> Unit) {
        clickHomeButtonListener = block
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        balanceText.text = viewModel.getTotalSumFromLocal()
        refresh.isRefreshing = true
        viewModel.getTotalSum()

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        refresh.setOnRefreshListener {
            viewModel.getTotalSum()
            refresh.isRefreshing = true
        }

        if (viewModel.isBalanceVisible) {
            showBalance()
        } else {
            hideBalance()
        }

        imgEye.setOnClickListener {
            if (viewModel.isBalanceVisible) {
                hideBalance()
            } else {
                showBalance()
            }
        }

        homeBtn.setOnClickListener {
            clickHomeButtonListener?.invoke()
        }

        myCardsImg.setOnClickListener {
            findNavController().navigate(R.id.action_basicScreen_to_myCardsScreen)
        }

        sendMoneyBtn.setOnClickListener {
            viewModel.getAllCardList()
            val bundle = Bundle()
            bundle.putString("direction_type", PaymentPageEnum.FROM_PAYMENT_SCREEN.name)
            findNavController().navigate(R.id.action_basicScreen_to_paymentPage, )
        }

        viewModel.totalSumLiveData.observe(viewLifecycleOwner, totalSumObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private fun hideBalance() = binding.scope {
        expensesText.gone()
        balanceTitle.invisible()
        currencyText.gone()
        balanceText.textSize = 20f
        balanceText.text = "balansni ko'rsatish"
        viewModel.isBalanceVisible = false
    }

    private fun showBalance() = binding.scope {
        balanceTitle.visible()
        expensesText.visible()
        currencyText.visible()
        viewModel.isBalanceVisible = true
        balanceText.textSize = 28f
        viewModel.getTotalSum()
    }

    private val totalSumObserver = Observer<String> {
        if (viewModel.isBalanceVisible) {
            binding.balanceText.text = it
        }
        binding.refresh.isRefreshing = false
    }

    private val errorMessageObserver = Observer<String> {
        binding.refresh.isRefreshing = false
    }
}