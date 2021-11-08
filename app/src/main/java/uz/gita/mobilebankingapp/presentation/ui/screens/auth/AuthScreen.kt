package uz.gita.mobilebankingapp.presentation.ui.screens.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenAuthBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class AuthScreen : Fragment(R.layout.screen_auth) {
    private val binding by viewBinding(ScreenAuthBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_authScreen_to_loginScreen)
        }
        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_authScreen_to_registerScreen)
        }
    }

}