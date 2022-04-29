package uz.gita.mobilebankingapp.presentation.ui.screens.auth

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
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.RegisterRequest
import uz.gita.mobilebankingapp.databinding.ScreenRegisterBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.auth.RegisterViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.auth.RegisterViewModelImpl
import uz.gita.mobilebankingapp.utils.disableError
import uz.gita.mobilebankingapp.utils.enableError
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val binding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModels<RegisterViewModelImpl>()
    private var isFirstNameReady = true
    private var isLastNameReady = true
    private var isPasswordReady = false
    private var isConfirmPasswordReady = false
    private var isPhoneNumberReady = false

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        /*firstNameEditText.apply {
            addTextChangedListener {
                firstNameEditTextLayout.disableError()
                it?.let {
                    isFirstNameReady = it.isNotEmpty()
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b && firstNameEditText.text.toString().isEmpty()) {
                    firstNameEditTextLayout.enableError()
                    firstNameEditTextLayout.error = "Firstname required"
                }
            }
        }*/
        /*lastNameEditText.addTextChangedListener {
            it?.let {
                isLastNameReady = it.length > 3
                check()
            }
        }*/
        phoneNumberEditText.apply {
            addTextChangedListener {
                phoneNumberEditTextLayout.disableError()
                it?.let {
                    isPhoneNumberReady = it.length == 13 && it.toString().startsWith("+998")
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b && (phoneNumberEditText.text.toString()
                        .isEmpty() || phoneNumberEditText.text.toString() == "+998")
                ) {
                    phoneNumberEditTextLayout.enableError()
                    phoneNumberEditTextLayout.error = "Phone is required"
                } else if (!b && !phoneNumberEditText.text.toString().startsWith("+998")) {
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

        passwordEditText.apply {
            addTextChangedListener {
                passwordEditTextLayout.disableError()
                it?.let {
                    isPasswordReady =
                        it.length >= 6 && it.toString() == confirmPasswordEditText.text.toString()
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b && (passwordEditText.text.toString().length < 6)) {
                    passwordEditTextLayout.enableError()
                    passwordEditTextLayout.error = "Password mus be at last 6 characters"
                } else if (!b && (passwordEditText.text.toString() != confirmPasswordEditText.text.toString())) {
                    confirmPasswordEditTextLayout.enableError()
                    confirmPasswordEditTextLayout.error = "Enter same passwords"
                } else {
                    confirmPasswordEditTextLayout.disableError()
                }
            }
        }

        confirmPasswordEditText.apply {
            addTextChangedListener {
                confirmPasswordEditTextLayout.disableError()
                it?.let {
                    isConfirmPasswordReady =
                        it.length >= 6 && it.toString() == passwordEditText.text.toString()
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b && (confirmPasswordEditText.text.toString().length < 6)) {
                    confirmPasswordEditTextLayout.enableError()
                    confirmPasswordEditTextLayout.error = "Password mus be at last 6 characters"
                } else if (!b && (passwordEditText.text.toString() != confirmPasswordEditText.text.toString())) {
                    confirmPasswordEditTextLayout.enableError()
                    confirmPasswordEditTextLayout.error = "Enter same passwords"
                } else {
                    confirmPasswordEditTextLayout.disableError()
                }
            }
        }

        registerButton.setOnClickListener {
            viewModel.registerUser(
                RegisterRequest(
                    firstNameEditText.text.toString(),
                    lastNameEditText.text.toString(),
                    phoneNumberEditText.text.toString(),
                    passwordEditText.text.toString(),
                )
            )
        }

        txtLogin.setOnClickListener {
            findNavController().navigate(RegisterScreenDirections.actionRegisterScreenToLoginScreen())
        }

        viewModel.disableRegisterLiveData.observe(viewLifecycleOwner, disableRegisterObserver)
        viewModel.enableRegisterLiveData.observe(viewLifecycleOwner, enableRegisterObserver)
        viewModel.errorLivaData.observe(viewLifecycleOwner, errorObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.successLiveData.observe(this@RegisterScreen, successObserver)
    }

    private fun check() {
        binding.registerButton.isEnabled =
            isFirstNameReady && isLastNameReady && isPasswordReady && isConfirmPasswordReady && isPhoneNumberReady
    }

    private val disableRegisterObserver = Observer<Unit> {
        binding.registerButton.isEnabled = false
    }
    private val enableRegisterObserver = Observer<Unit> {
        binding.registerButton.isEnabled = true
    }
    private val errorObserver = Observer<String> { errorMessage ->
        binding.progress.hide()
        if (errorMessage.equals(getString(R.string.error_register_phone_number_already_registered))) {
            binding.phoneNumberEditTextLayout.enableError()
            binding.phoneNumberEditTextLayout.error = "Phone is already available"
        }
    }
    private val progressObserver = Observer<Boolean> {
        if (it) binding.progress.show()
        else binding.progress.hide()
    }
    private val successObserver = Observer<String> {
        findNavController().navigate(RegisterScreenDirections.actionRegisterScreenToVerifyScreen())
    }
}