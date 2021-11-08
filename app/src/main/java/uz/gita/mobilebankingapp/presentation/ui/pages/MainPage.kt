package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.PageMainBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class MainPage : Fragment(R.layout.page_main) {
    private val binding by viewBinding(PageMainBinding::bind)

    private var clickHomeButtonListener: (() -> Unit)? = null
    fun setOnClickHomeButtonListener(block: () -> Unit) {
        clickHomeButtonListener = block
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        homeBtn.setOnClickListener {
            clickHomeButtonListener?.invoke()
        }

        myCardsImg.setOnClickListener {
            findNavController().navigate(R.id.action_basicScreen_to_myCardsScreen)
        }

        profileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_basicScreen_to_profileScreen)
        }

        sendMoneyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_basicScreen_to_fillSendMoneyScreen)
        }
    }
}