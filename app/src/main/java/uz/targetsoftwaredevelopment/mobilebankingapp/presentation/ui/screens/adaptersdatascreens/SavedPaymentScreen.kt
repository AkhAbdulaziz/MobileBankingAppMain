package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.adaptersdatascreens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenSavedPaymentBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

@AndroidEntryPoint
class SavedPaymentScreen : Fragment(R.layout.screen_saved_payment) {
    private val binding by viewBinding(ScreenSavedPaymentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)


    }
}