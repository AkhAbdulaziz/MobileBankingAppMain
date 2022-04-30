package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenSplashBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.SplashViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main.SplashViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

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
