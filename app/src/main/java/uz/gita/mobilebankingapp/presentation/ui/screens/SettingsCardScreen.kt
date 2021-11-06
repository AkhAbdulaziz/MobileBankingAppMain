package uz.gita.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.model.card_req_res.request.EditCardRequest
import uz.gita.mobilebankingapp.databinding.ScreenCardSettingsBinding
import uz.gita.mobilebankingapp.presentation.adapter.CardBackgroundsPagerAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.SettingsCardViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.SettingsCardViewModelImpl
import uz.gita.mobilebankingapp.utils.hideKeyboardFrom
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class SettingsCardScreen : Fragment(R.layout.screen_card_settings) {
    private val binding by viewBinding(ScreenCardSettingsBinding::bind)
    private val viewModel: SettingsCardViewModel by viewModels<SettingsCardViewModelImpl>()
    private lateinit var bgsList: ArrayList<Int>
    private lateinit var pagerAdapter: CardBackgroundsPagerAdapter
    private var cardPan = ""

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

        textCardName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFrom(requireContext(), view)
            }
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        arguments?.let {
            cardPan = it.getString("pan", "")
        }

        readyChangesBtn.setOnClickListener {
            viewModel.editCard(
                EditCardRequest(
                    cardPan, textCardName.text.toString()
                )
            )
        }
    }
}