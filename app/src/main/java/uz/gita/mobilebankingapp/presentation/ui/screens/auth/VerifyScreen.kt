package uz.gita.mobilebankingapp.presentation.ui.screens.auth

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.VerifyUserRequest
import uz.gita.mobilebankingapp.databinding.ScreenVerifyBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.auth.VerifyScreenViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.auth.VerifyScreenViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyScreenViewModel by viewModels<VerifyScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        val phoneNumber998 = "${viewModel.getUserPhoneNumber().substring(0, 4)}"
        val phoneNumberCode = "${viewModel.getUserPhoneNumber().substring(4, 6)}"
        val phoneNumberLast = "${viewModel.getUserPhoneNumber().substring(11)}"

        infoText.text =
            "Tekshiruv kodi quyidagi\nraqamga yuborildi\n$phoneNumber998 $phoneNumberCode *** ** $phoneNumberLast"

        verificationCodeEditText.addTextChangedListener {
            it?.let {
                verifyBtn.isEnabled = it.length == 6
            }
        }

        verificationCodeEditText.focus()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            verificationCodeEditText.isFocusedByDefault = true
        }

        verifyBtn.setOnClickListener {
            viewModel.userVerify(
                VerifyUserRequest(
                    viewModel.getUserPhoneNumber(),
                    verificationCodeEditText.text.toString()
                )
            )
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.openMainScreenLiveData.observe(viewLifecycleOwner, openMainScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private val openMainScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_verifyScreen_to_basicScreen)
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

}