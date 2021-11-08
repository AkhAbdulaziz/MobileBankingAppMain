package uz.gita.mobilebankingapp.presentation.ui.screens.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {
    private val binding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel : RegisterViewModel by viewModels<RegisterViewModelImpl>()
    private var boolFirstName = false
    private var boolLastName = false
    private var boolPassword = false
    private var boolConfirmPassword = false
    private var boolPhoneNumber = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        registerButton.isEnabled = false
        firstNameEditText.addTextChangedListener {
            it?.let {
                boolFirstName = it.length > 3
                check()
            }
        }
        lastNameEditText.addTextChangedListener {
            it?.let {
                boolLastName = it.length > 3
                check()
            }
        }
        passwordEditText.addTextChangedListener {
            it?.let {
                boolPassword = it.length > 6 && it.toString() == confirmPasswordEditText.text.toString()
                check()
            }
        }
        confirmPasswordEditText.addTextChangedListener {
            it?.let {
                boolConfirmPassword = it.length > 6 && it.toString() == passwordEditText.text.toString()
                check()
            }
        }
        phoneNumberEditText.addTextChangedListener {
            it?.let {
                boolPhoneNumber = it.length == 13 && it.toString().startsWith("+998")
                check()
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

        viewModel.disableRegisterLiveData.observe(viewLifecycleOwner,disableRegisterObserver)
        viewModel.enableRegisterLiveData.observe(viewLifecycleOwner,enableRegisterObserver)
        viewModel.errorLivaData.observe(viewLifecycleOwner,errorObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner,progressObserver)
        viewModel.successLiveData.observe(viewLifecycleOwner,successObserver)
    }

    private fun check() {
        binding.registerButton.isEnabled = boolFirstName && boolLastName && (boolPassword || boolConfirmPassword) && boolPhoneNumber
    }

    private val disableRegisterObserver = Observer<Unit> {
        binding.registerButton.isEnabled = false
    }
    private val enableRegisterObserver = Observer<Unit> {
        binding.registerButton.isEnabled = true
    }
    private val errorObserver = Observer<String> {
        Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
    }
    private val progressObserver = Observer<Boolean> {
        if (it) binding.progress.show()
        else binding.progress.hide()
    }
    private val successObserver = Observer<String> {
        findNavController().navigate(R.id.action_registerScreen_to_verifyScreen)
    }
}