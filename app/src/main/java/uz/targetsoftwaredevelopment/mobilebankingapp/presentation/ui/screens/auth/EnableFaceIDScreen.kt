package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.auth

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenEnableFaceidBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.auth.EnableFaceIDViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.auth.EnableFaceIDViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

@AndroidEntryPoint
class EnableFaceIDScreen : Fragment(R.layout.screen_enable_faceid) {
    private val binding by viewBinding(ScreenEnableFaceidBinding::bind)
    private val viewModel: EnableFaceIDViewModel by viewModels<EnableFaceIDViewModelImpl>()
    private val args : EnableFaceIDScreenArgs by navArgs()

    private var faceToLogin: Boolean = false
    private var confirmPayment: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        tvSkip.setOnClickListener {
            findNavController().navigate(EnableFaceIDScreenDirections.actionEnableFaceIDScreenToBasicScreen(args.isNewUser))
        }

        switchBtnUseFaceid.apply {
            setOnCheckedChangeListener { _, isChecked ->
                faceToLogin = isChecked
                highlightImage()
                viewModel.changeFaceIDPermission(isChecked)

                if (isChecked) {
                    this.setBackColorRes(R.color.lightBaseColor)
                } else {
                    this.setBackColorRes(R.color.greyBg)
                }
            }
        }

        switchBtnConfirmByFingerprint.apply {
            setOnCheckedChangeListener { _, isChecked ->
                confirmPayment = isChecked
                highlightImage()
                viewModel.changeConfirmationByFingerPrintPermission(isChecked)

                if (isChecked) {
                    this.setBackColorRes(R.color.lightBaseColor)
                } else {
                    this.setBackColorRes(R.color.greyBg)
                }
            }
        }

        btnDone.setOnClickListener {
            findNavController().navigate(EnableFaceIDScreenDirections.actionEnableFaceIDScreenToBasicScreen(args.isNewUser))
        }
    }

    private fun highlightImage() = binding.scope {
        btnDone.isEnabled = faceToLogin || confirmPayment

        if (faceToLogin) {
            imgFaceID.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.lightBaseColor
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            imgFaceID.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.greyBg
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }
}