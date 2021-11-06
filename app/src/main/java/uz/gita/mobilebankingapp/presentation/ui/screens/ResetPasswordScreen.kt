package uz.gita.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenResetPasswordBinding

@AndroidEntryPoint
class ResetPasswordScreen : Fragment(R.layout.screen_reset_password) {
    private val binding by viewBinding(ScreenResetPasswordBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}