package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.enums.StartScreenEnum
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.gita.mobilebankingapp.databinding.ScreenPaymePeopleBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.AdsPrizesPagerAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.PaymePeopleViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.PaymePeopleViewModelImpl
import uz.gita.mobilebankingapp.utils.adsPrizesImagesList
import uz.gita.mobilebankingapp.utils.dpToPx
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class PaymePeopleScreen : Fragment(R.layout.screen_payme_people) {
    private val binding by viewBinding(ScreenPaymePeopleBinding::bind)
    private val viewModel: PaymePeopleViewModel by viewModels<PaymePeopleViewModelImpl>()
    private lateinit var pagerAdapter: AdsPrizesPagerAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        pagerAdapter = AdsPrizesPagerAdapter(adsPrizesImagesList, childFragmentManager, lifecycle)
        adsPrizesPager.apply {
            adapter = pagerAdapter
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2  // make sure left/right item is rendered
        }
        // increase this offset to show more of left/right
        val offsetPx = 16.dpToPx(resources.displayMetrics)
        adsPrizesPager.setPadding(offsetPx, 0, offsetPx, 0)

        // increase this offset to increase distance between 2 items
        val pageMarginPx = 2.dpToPx(resources.displayMetrics)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        adsPrizesPager.setPageTransformer(marginTransformer)

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                delay(4000L)
                if (adsPrizesPager.currentItem == adsPrizesImagesList.size - 1) {
                    adsPrizesPager.currentItem = 0
                } else {
                    adsPrizesPager.currentItem += 1
                }
            }
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        layoutMorePrizes.setOnClickListener {
            showToast("More about prizes")
        }

        imgOurPartners.setOnClickListener {
            showToast("Our partners")
        }

        txtRulesOfThePaymePeople.apply {
            setOnTouchListener { view, motionEvent ->
                val action = motionEvent.action

                if (action == MotionEvent.ACTION_MOVE) {
                    txtRulesOfThePaymePeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.lightBaseColor
                        )
                    )
                }
                if (action == MotionEvent.ACTION_UP) {
                    txtRulesOfThePaymePeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.lightBaseColor
                        )
                    )
                    showToast("Rules of the Payme People promotion")
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    txtRulesOfThePaymePeople.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.baseColor
                        )
                    )
                }
                true
            }
        }

        viewModel.openLoginScreenLiveData.observe(viewLifecycleOwner, openLoginScreenObserver)
    }

    private val openLoginScreenObserver = Observer<LogoutResponse> {
        findNavController().navigate(
            PaymePeopleScreenDirections.actionPaymePeopleScreenToPinCodeScreen(
                StartScreenEnum.MAIN
            )
        )
    }
}