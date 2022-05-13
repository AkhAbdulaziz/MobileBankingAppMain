package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.card

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
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.AddCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenAddCardBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.CardBackgroundsPagerAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.AddCardViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card.AddCardViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.*

@AndroidEntryPoint
class AddCardScreen : Fragment(R.layout.screen_add_card) {
    private val binding by viewBinding(ScreenAddCardBinding::bind)
    private val viewModel: AddCardViewModel by viewModels<AddCardViewModelImpl>()
    private lateinit var pagerAdapter: CardBackgroundsPagerAdapter
    private var isReadyCardNumber = false
    private var isReadyCardValidate = false

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        pagerAdapter =
            CardBackgroundsPagerAdapter(cardBgImagesList, childFragmentManager, lifecycle)
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
            progressBar.visible()
            viewModel.addCard(
                AddCardRequest(
                    textCardNumber.text.toString(),
                    textCardValidate.text.toString(),
                    if (textCardName.text.toString().isEmpty()) {
                        "new1"
                    } else {
                        textCardName.text.toString()
                    }
                ),
                cardBackgroundsViewPager.currentItem
            )
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.openVerifyCardScreenLiveData.observe(
            this@AddCardScreen,
            openVerifyCardScreenObserver
        )
        viewModel.closeScreenLiveData.observe(viewLifecycleOwner, closeScreenObserver)
        viewModel.openLoginScreenLiveData.observe(viewLifecycleOwner, openLoginScreenObserver)
    }

    private val openLoginScreenObserver = Observer<Unit> {
        findNavController().navigate(
            AddCardScreenDirections.actionAddCardScreenToPinCodeScreen(
                StartScreenEnum.MAIN
            )
        )
    }

    private fun check() {
        binding.addCardBtn.isEnabled =
            isReadyCardValidate && isReadyCardNumber
    }

    private val closeScreenObserver = Observer<Unit> {
        binding.progressBar.gone()
        findNavController().popBackStack()
    }

    private val openVerifyCardScreenObserver = Observer<Unit> {
        findNavController().navigate(
            AddCardScreenDirections.actionAddCardScreenToVerifyCardScreen(
                binding.cardBackgroundsViewPager.currentItem
            )
        )
    }

    private val errorMessageObserver = Observer<String> {
        showFancyToast(
            it,
            FancyToast.LENGTH_SHORT,
            FancyToast.ERROR
        )
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