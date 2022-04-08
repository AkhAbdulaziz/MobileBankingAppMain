package uz.gita.mobilebankingapp.presentation.ui.screens.auth

import android.annotation.SuppressLint
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
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.LoginRequest
import uz.gita.mobilebankingapp.databinding.ScreenLoginBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.auth.LoginViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.auth.LoginViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()
    private var isReadyPassword = false
    private var isReadyPhoneNumber = false

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        passwordEditText.addTextChangedListener {
            it?.let {
                isReadyPassword = it.length > 3
                check()
            }
        }
        phoneNumberEditText.addTextChangedListener {
            it?.let {
                isReadyPhoneNumber = it.length > 10 && it.contains("+998")
                check()
            }
        }
        loginButton.isEnabled = false
        loginButton.setOnClickListener {
            viewModel.userLogin(
                LoginRequest(
                    phoneNumberEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            )
        }

        switchBtnUntrustedDevice.apply {
            setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    this.setBackColorRes(R.color.baseColor)
                } else {
                    this.setBackColorRes(R.color.greyBg)
                }
            }
        }

        viewModel.enableLoginButtonLiveData.observe(viewLifecycleOwner, enableLoginButtonObserver)
        viewModel.disableLoginButtonLiveData.observe(viewLifecycleOwner, disableLoginButtonObserver)
        viewModel.showProgressLiveData.observe(viewLifecycleOwner, showProgressObserver)
        viewModel.hideProgressLiveData.observe(viewLifecycleOwner, hideProgressObserver)
        viewModel.openVerifyScreenLiveData.observe(this@LoginScreen, openVerifyScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private fun check() {
        binding.loginButton.isEnabled =
            isReadyPassword && isReadyPhoneNumber
    }

    private val enableLoginButtonObserver = Observer<Unit> {
        binding.loginButton.isEnabled = true
    }
    private val disableLoginButtonObserver = Observer<Unit> {
        binding.loginButton.isEnabled = false
    }
    private val showProgressObserver = Observer<Unit> {
        binding.progress.show()
    }
    private val hideProgressObserver = Observer<Unit> {
        binding.progress.hide()
    }
    private val openVerifyScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_loginScreen_to_verifyScreen)
    }
    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }
}