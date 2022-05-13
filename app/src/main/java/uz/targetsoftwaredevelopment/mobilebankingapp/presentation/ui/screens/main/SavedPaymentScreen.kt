package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenSavedPaymentBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.SavedPaymentViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main.SavedPaymentViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast

@AndroidEntryPoint
class SavedPaymentScreen : Fragment(R.layout.screen_saved_payment) {
    private val binding by viewBinding(ScreenSavedPaymentBinding::bind)
    private val viewModel: SavedPaymentViewModel by viewModels<SavedPaymentViewModelImpl>()
    private val args: SavedPaymentScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        val data = args.savedPaymentData

        imgReceiverCompanyLogo.setImageResource(data.logo)
        txtReceiverCompanyName.text = data.logoName
        cardNumberEditText.setText(data.phone)
        moneyAmountEditText.setText("${data.moneyAmount}")

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        imgContact.setOnClickListener {
            Permissions.check(requireContext(), arrayOf(
                android.Manifest.permission.READ_CONTACTS
            ), null, null,
                object : PermissionHandler() {
                    override fun onGranted() {
                        findNavController().navigate(SavedPaymentScreenDirections.actionSavedPaymentScreenToContactsScreen())
                    }
                }
            )
        }

        btnNext.setOnClickListener {
            showFancyToast(
                "Next",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }
    }
}