package uz.gita.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.enums.StartScreenEnum
import uz.gita.mobilebankingapp.databinding.ScreenSplashBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.SplashViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.SplashViewModelImpl
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val binding by viewBinding(ScreenSplashBinding::bind)
    private val viewModel: SplashViewModel by viewModels<SplashViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        viewModel.openAuthScreenLiveData.observe(viewLifecycleOwner, openAuthScreenObserver)
        viewModel.openMainScreenLiveData.observe(viewLifecycleOwner, openMainScreenObserver)
    }

    private val openAuthScreenObserver = Observer<Unit> {
        findNavController().navigate(SplashScreenDirections.actionSplashScreenToLoginScreen())
    }

    private val openMainScreenObserver = Observer<Unit> {
        findNavController().navigate(
            SplashScreenDirections.actionSplashScreenToPinCodeScreen(
                StartScreenEnum.MAIN, false
            )
        )
    }
}
