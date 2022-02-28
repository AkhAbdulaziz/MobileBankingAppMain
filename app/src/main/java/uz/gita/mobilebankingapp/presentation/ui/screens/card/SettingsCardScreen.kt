package uz.gita.mobilebankingapp.presentation.ui.screens.card

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kyleduo.switchbutton.SwitchButton
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.EditCardRequest
import uz.gita.mobilebankingapp.databinding.ScreenCardSettingsBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.CardBackgroundsPagerAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SettingsCardViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.SettingsCardViewModelImpl
import uz.gita.mobilebankingapp.utils.cardBgImagesList
import uz.gita.mobilebankingapp.utils.dpToPx
import uz.gita.mobilebankingapp.utils.hideKeyboardFrom
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class SettingsCardScreen : Fragment(R.layout.screen_card_settings) {
    private val binding by viewBinding(ScreenCardSettingsBinding::bind)
    private val viewModel: SettingsCardViewModel by viewModels<SettingsCardViewModelImpl>()
//    private val bgsList = ArrayList<Int>()
    private lateinit var pagerAdapter: CardBackgroundsPagerAdapter
    private val args: SettingsCardScreenArgs by navArgs()

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

        val mainCard: CardData? = viewModel.getMainCardData()
        if (mainCard != null) {
            if (mainCard.pan == args.cardData.pan) {
                txtIsChecked.text = "Remove from main"
                imgIsChecked.setImageResource(R.drawable.ic_checked)
            } else {
                txtIsChecked.text = "Make main"
                imgIsChecked.setImageResource(R.drawable.ic_unchecked)
            }
        }

        setCardMainLayout.setOnClickListener {
            if (mainCard == null) {
                txtIsChecked.text = "Remove from main"
                imgIsChecked.setImageResource(R.drawable.ic_checked)
            } else if (mainCard.pan != args.cardData.pan) {
                txtIsChecked.text = "Remove from main"
                imgIsChecked.setImageResource(R.drawable.ic_checked)
            } else {
                txtIsChecked.text = "Make main"
                imgIsChecked.setImageResource(R.drawable.ic_unchecked)
            }
            viewModel.changeMainCard(args.cardData.pan!!)
        }

        textCardName.setText(args.cardData.cardName!!)
        textCardName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFrom(requireContext(), view)
            }
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        readyChangesBtn.setOnClickListener {
            viewModel.editCard(
                EditCardRequest(
                    args.cardData.pan!!, textCardName.text.toString()
                ),
                args.cardData.id!!,
                cardBackgroundsViewPager.currentItem
            )
        }

        switchBtnHideBalance.apply {
            setOnCheckedChangeListener { compoundButton, checked ->
                if (checked) {
                    this.setBackColorRes(R.color.baseColor)
                } else {
                    this.setBackColorRes(R.color.greyBg)
                }
            }
        }

        viewModel.closeScreenLiveData.observe(viewLifecycleOwner, closeScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    /*private fun fillBgList() {
        bgsList.apply {
            add(R.drawable.card_theme1)
            add(R.drawable.card_theme2)
            add(R.drawable.card_theme3)
            add(R.drawable.card_theme4)
            add(R.drawable.card_theme5)
            add(R.drawable.card_theme6)
            add(R.drawable.card_theme7)
            add(R.drawable.card_theme8)
            add(R.drawable.card_theme9)
            add(R.drawable.card_theme10)
            add(R.drawable.card_theme11)
            add(R.drawable.card_theme12)
            add(R.drawable.card_theme13)
            add(R.drawable.card_theme14)
            add(R.drawable.card_theme15)
            add(R.drawable.card_theme16)
        }
    }*/

    private val closeScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }

    private val errorMessageObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }
}
