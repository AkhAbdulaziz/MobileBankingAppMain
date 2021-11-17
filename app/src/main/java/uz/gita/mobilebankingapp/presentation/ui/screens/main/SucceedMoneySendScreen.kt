package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenSucceedMoneySendBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class SucceedMoneySendScreen : Fragment(R.layout.screen_succeed_money_send) {
    private val binding by viewBinding(ScreenSucceedMoneySendBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        val myScope = CoroutineScope(Dispatchers.Default)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(SucceedMoneySendScreenDirections.actionSucceedMoneySendScreenToBasicScreen())
                }
            })

        backToMainPage.setOnClickListener {
            myScope.launch {
                backToMainPage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    )
                )
                delay(1000)
                backToMainPage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.baseColor
                    )
                )
            }
            findNavController().navigate(SucceedMoneySendScreenDirections.actionSucceedMoneySendScreenToBasicScreen())
        }
    }
}