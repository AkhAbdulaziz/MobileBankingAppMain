package uz.gita.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenMainBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {
    private val binding by viewBinding(ScreenMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {

        myCardsImg.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_myCardsScreen)
        }

        profileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_profileScreen)
        }

        sendMoneyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_fillSendMoneyScreen)
        }
    }
}