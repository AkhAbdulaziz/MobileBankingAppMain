package uz.gita.mobilebankingapp.presentation.ui.screens.card

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.EditCardRequest
import uz.gita.mobilebankingapp.databinding.ScreenCardSettingsBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.CardBackgroundsPagerAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SettingsCardViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.SettingsCardViewModelImpl
import uz.gita.mobilebankingapp.utils.hideKeyboardFrom
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class SettingsCardScreen : Fragment(R.layout.screen_card_settings) {
    private val binding by viewBinding(ScreenCardSettingsBinding::bind)
    private val viewModel: SettingsCardViewModel by viewModels<SettingsCardViewModelImpl>()
    private lateinit var bgsList: ArrayList<Int>
    private lateinit var pagerAdapter: CardBackgroundsPagerAdapter
    private val args: SettingsCardScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        bgsList = ArrayList()
        bgsList.add(R.drawable.card_bg1)
        bgsList.add(R.drawable.card_bg2)
        bgsList.add(R.drawable.card_bg1)
        bgsList.add(R.drawable.card_bg2)

        pagerAdapter = CardBackgroundsPagerAdapter(bgsList, childFragmentManager, lifecycle)
        cardBackgroundsViewPager.adapter = pagerAdapter

        val mainCard: CardData? = viewModel.getMainCardData()
        if (mainCard != null) {
            if (mainCard.pan == args.cardData.pan) {
                txtIsChecked.text = "Asosiydan olib tashlash"
                imgIsChecked.setImageResource(R.drawable.ic_checked)
            } else {
                txtIsChecked.text = "Asosiy qilish"
                imgIsChecked.setImageResource(R.drawable.ic_unchecked)
            }
        }

        setCardMainLayout.setOnClickListener {
            if (mainCard == null) {
                txtIsChecked.text = "Asosiydan olib tashlash"
                imgIsChecked.setImageResource(R.drawable.ic_checked)
            } else if (mainCard.pan != args.cardData.pan) {
                txtIsChecked.text = "Asosiydan olib tashlash"
                imgIsChecked.setImageResource(R.drawable.ic_checked)
            } else {
                txtIsChecked.text = "Asosiy qilish"
                imgIsChecked.setImageResource(R.drawable.ic_unchecked)
            }
            viewModel.changeMainCard(args.cardData.pan!!)
        }

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
                )
            )
        }

        viewModel.closeScreenLiveData.observe(viewLifecycleOwner, closeScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private val closeScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }

    private val errorMessageObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }
}
