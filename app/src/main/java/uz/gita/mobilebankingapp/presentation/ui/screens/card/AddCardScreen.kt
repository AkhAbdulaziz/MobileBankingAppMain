package uz.gita.mobilebankingapp.presentation.ui.screens.card

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
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.AddCardRequest
import uz.gita.mobilebankingapp.databinding.ScreenAddCardBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.CardBackgroundsPagerAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.AddCardViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.AddCardViewModelImpl
import uz.gita.mobilebankingapp.utils.hideKeyboardFrom
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class AddCardScreen : Fragment(R.layout.screen_add_card) {
    private val binding by viewBinding(ScreenAddCardBinding::bind)
    private val viewModel: AddCardViewModel by viewModels<AddCardViewModelImpl>()
    private lateinit var bgsList: ArrayList<Int>
    private lateinit var pagerAdapter: CardBackgroundsPagerAdapter
    private var isReadyCardNumber = false
    private var isReadyCardValidate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        bgsList = ArrayList()
        bgsList.add(R.drawable.card_bg1)
        bgsList.add(R.drawable.card_bg2)
        bgsList.add(R.drawable.card_bg1)
        bgsList.add(R.drawable.card_bg2)

        pagerAdapter = CardBackgroundsPagerAdapter(bgsList, childFragmentManager, lifecycle)
        cardBackgroundsViewPager.adapter = pagerAdapter

        cardBackgroundsViewPager.offscreenPageLimit = 3
        val pageMargin = 16
        val pageOffset = 16
        cardBackgroundsViewPager.setPageTransformer { page, position ->
            val myOffset: Float = position * -(2 * pageOffset + pageMargin)
            if (position < -1) {
                page.translationX = -myOffset
            } else if (position <= 1) {
                val scaleFactor =
                    Math.max(0.7f, 1 - Math.abs(position - 0.14285715f))
                page.translationX = myOffset
                page.scaleY = scaleFactor
                page.alpha = scaleFactor
            } else {
                page.alpha = 0f
                page.translationX = myOffset
            }
        }

        loadHideKeyboardFunctions()

        textCardNumber.addTextChangedListener {
            it?.let {
                isReadyCardNumber = it.toString().length == 16
                check()
            }
        }

        textCardValidate.addTextChangedListener {
            it?.let {
                isReadyCardValidate = it.toString().length == 5
                check()
            }
        }

        addCardBtn.isEnabled = false
        addCardBtn.setOnClickListener {
            viewModel.addCard(
                AddCardRequest(
                    textCardNumber.text.toString(),
                    textCardValidate.text.toString(),
                    if (textCardName.text.toString().isEmpty()) {
                        "new1"
                    } else {
                        textCardName.text.toString()
                    }
                )
            )
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.disableAddButtonLiveData.observe(viewLifecycleOwner, disableAddButtonObserver)
        viewModel.enableAddButtonLiveData.observe(viewLifecycleOwner, enableAddButtonObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.openVerifyCardScreenLiveData.observe(
            viewLifecycleOwner,
            openVerifyCardScreenObserver
        )
    }

    private fun check() {
        binding.addCardBtn.isEnabled =
            isReadyCardValidate && isReadyCardNumber
    }

    private val openVerifyCardScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_addCardScreen_to_verifyCardScreen)
    }

    private val disableAddButtonObserver = Observer<Unit> {
        binding.addCardBtn.isEnabled = false
    }

    private val enableAddButtonObserver = Observer<Unit> {
        binding.addCardBtn.isEnabled = true
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

    private fun loadHideKeyboardFunctions() = binding.scope {
        textCardNumber.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFrom(requireContext(), view)
            }
        }

        textCardValidate.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFrom(requireContext(), view)
            }
        }

        textCardName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFrom(requireContext(), view)
            }
        }
    }
}