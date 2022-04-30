package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.LoginRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenLoginBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.auth.LoginViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.auth.LoginViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.disableError
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.enableError
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModelImpl>()

    private var isReadyPassword = false
    private var isReadyPhoneNumber = false

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        passwordEditText.apply {
            addTextChangedListener {
                passwordEditTextLayout.disableError()
                it?.let {
                    isReadyPassword = it.length >= 6
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b && (passwordEditText.text.toString().length < 6)) {
                    passwordEditTextLayout.enableError()
                    passwordEditTextLayout.error = "Password mus be at last 6 characters"
                }
            }
        }

        phoneNumberEditText.apply {
            addTextChangedListener {
                phoneNumberEditTextLayout.disableError()
                it?.let {
                    isReadyPhoneNumber = it.length == 13 && it.contains("+998")
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b && (phoneNumberEditText.text.toString()
                        .isEmpty() || phoneNumberEditText.text.toString() == "+998")
                ) {
                    phoneNumberEditTextLayout.enableError()
                    phoneNumberEditTextLayout.error = "Phone is required"
                } else if (!b && !phoneNumberEditText.text.toString().contains("+998")) {
                    phoneNumberEditTextLayout.enableError()
                    phoneNumberEditTextLayout.error = "Need to starts with +998"
                } else if (!b && phoneNumberEditText.text.toString().length != 13) {
                    phoneNumberEditTextLayout.enableError()
                    phoneNumberEditTextLayout.error = "Phone is not valid"
                } else {
                    phoneNumberEditTextLayout.disableError()
                }
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

        txtRegister.setOnClickListener {
            findNavController().navigate(LoginScreenDirections.actionLoginScreenToRegisterScreen())
        }

        switchBtnUntrustedDevice.apply {
            setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    this.setBackColorRes(R.color.lightBaseColor)
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
        findNavController().navigate(
            LoginScreenDirections.actionLoginScreenToPinCodeScreen(
                StartScreenEnum.LOGIN, false
            )
        )
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        binding.progress.hide()
        if (errorMessage.equals(getString(R.string.error_login_phone_number_not_available))) {
            binding.phoneNumberEditTextLayout.enableError()
            binding.phoneNumberEditTextLayout.error = "Phone is incorrect"
        }
        if (errorMessage.equals(getString(R.string.error_login_password_incorrect))) {
            binding.passwordEditTextLayout.enableError()
            binding.passwordEditTextLayout.error = "Password is incorrect"
        }
    }
}
