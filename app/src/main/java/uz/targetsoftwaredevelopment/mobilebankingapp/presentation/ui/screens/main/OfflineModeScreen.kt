package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenOfflineModeBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.*

class OfflineModeScreen : Fragment(R.layout.screen_offline_mode) {
    private val binding by viewBinding(ScreenOfflineModeBinding::bind)
    private val checkInternetReceiver = CheckInternetReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        innerView.innerViewCardView.setOnClickListener {
            showFancyToast(
                "Payme Go",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        btnRepeat.setOnClickListener {
            if(isConnected()){
                 findNavController().popBackStack()
            }
        }
    }

    override fun onStart() {
        val filter: IntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(checkInternetReceiver, filter)
        super.onStart()
    }

    override fun onDestroy() {
        requireContext().unregisterReceiver(checkInternetReceiver)
        super.onDestroy()
    }
}