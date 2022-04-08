package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenOfflineModeBinding
import uz.gita.mobilebankingapp.utils.CheckInternetReceiver
import uz.gita.mobilebankingapp.utils.isConnected
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

class OfflineModeScreen : Fragment(R.layout.screen_offline_mode) {
    private val binding by viewBinding(ScreenOfflineModeBinding::bind)
    private val checkInternetReceiver = CheckInternetReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        innerView.innerViewCardView.setOnClickListener {
            showToast("Payme Go")
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