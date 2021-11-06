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
import uz.gita.mobilebankingapp.data.model.user_req_res.request.VerifyUserRequest
import uz.gita.mobilebankingapp.databinding.ScreenVerifyBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.VerifyScreenViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.VerifyScreenViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyScreenViewModel by viewModels<VerifyScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        sendBtn.setOnClickListener {
            viewModel.userVerify(
                VerifyUserRequest(
                    viewModel.getUserPhoneNumber(),
                    verificationCodeEditText.text.toString()
                )
            )
        }

        viewModel.openMainScreenLiveData.observe(viewLifecycleOwner, openMainScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private val openMainScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_verifyScreen_to_mainScreen)
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

}