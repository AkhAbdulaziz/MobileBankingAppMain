package uz.gita.mobilebankingapp.presentation.ui.screens.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.AddCardRequest
import uz.gita.mobilebankingapp.databinding.ScreenAddCardBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.CardBackgroundsPagerAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.AddCardViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.AddCardViewModelImpl
import uz.gita.mobilebankingapp.utils.*

@AndroidEntryPoint
class AddCardScreen : Fragment(R.layout.screen_add_card) {
    private val binding by viewBinding(ScreenAddCardBinding::bind)
    private val viewModel: AddCardViewModel by viewModels<AddCardViewModelImpl>()
//    private lateinit var bgsList: ArrayList<Int>
    private lateinit var pagerAdapter: CardBackgroundsPagerAdapter
    private var isReadyCardNumber = false
    private var isReadyCardValidate = false

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        pagerAdapter = CardBackgroundsPagerAdapter(cardBgImagesList, childFragmentManager, lifecycle)
        cardBackgroundsViewPager.apply {
            adapter = pagerAdapter
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2  // make sure left/right item is rendered
        }
        // increase this offset to show more of left/right
        val offsetPx = 16.dpToPx(resources.displayMetrics)
        cardBackgroundsViewPager.setPadding(offsetPx, 0, offsetPx, 0)

        // increase this offset to increase distance between 2 items
        val pageMarginPx = 2.dpToPx(resources.displayMetrics)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        cardBackgroundsViewPager.setPageTransformer(marginTransformer)
        TabLayoutMediator(cardBackgroundsTabLayout, cardBackgroundsViewPager) { tab, pos ->

        }.attach()

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
            this@AddCardScreen,
            openVerifyCardScreenObserver
        )
        viewModel.closeScreenLiveData.observe(viewLifecycleOwner, closeScreenObserver)
    }

    private fun check() {
        binding.addCardBtn.isEnabled =
            isReadyCardValidate && isReadyCardNumber
    }

    private val closeScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }

    private val openVerifyCardScreenObserver = Observer<Unit> {
        findNavController().navigate(
            AddCardScreenDirections.actionAddCardScreenToVerifyCardScreen(
                binding.cardBackgroundsViewPager.currentItem
            )
        )
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