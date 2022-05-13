package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenResetPasswordBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

@AndroidEntryPoint
class ResetPasswordScreen : Fragment(R.layout.screen_reset_password) {
    private val binding by viewBinding(ScreenResetPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope{

    }
}