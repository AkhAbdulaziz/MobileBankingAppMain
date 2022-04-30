package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.card

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.VerifyCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.ResendRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenVerifyCardBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.VerifyCardViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card.VerifyCardViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.*

@AndroidEntryPoint
class VerifyCardScreen : Fragment(R.layout.screen_verify_card) {
    private val binding by viewBinding(ScreenVerifyCardBinding::bind)
    private val viewModel: VerifyCardViewModel by viewModels<VerifyCardViewModelImpl>()
    private val args: VerifyCardScreenArgs by navArgs()
    private var phoneNumber = ""
    private var userPassword = ""
    private var phoneNumber998 = ""
    private var phoneNumberCode = ""
    private var phoneNumberLast = ""
    private var minute = 60
    private var isBreak = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        waitingCodeTime.isClickable = false
        viewModel.getUserPhoneNumber()
        startTime()

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
            progressBar.visible()
            viewModel.getCurrentPan()
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        waitingCodeTime.setOnClickListener {
            viewModel.getUserPassword()
            viewModel.resendCode(
                ResendRequest(
                    phoneNumber,
                    userPassword
                )
            )
            timber("phone = $phoneNumber password = $userPassword", "GGG")
        }

        viewModel.exitScreenLiveData.observe(viewLifecycleOwner, exitScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.resendCodeLiveData.observe(viewLifecycleOwner, resendCodeObserver)
        viewModel.userPhoneNumberLiveData.observe(viewLifecycleOwner, userPhoneNumberObserver)
        viewModel.userPasswordLiveData.observe(viewLifecycleOwner, userPasswordObserver)
        viewModel.currentPanLiveData.observe(viewLifecycleOwner, currentPanObserver)
    }

    private val exitScreenObserver = Observer<Unit> {
        binding.progressBar.invisible()
        findNavController().popBackStack()
    }

    private val userPhoneNumberObserver = Observer<String> { userPhone ->
        phoneNumber = userPhone
        phoneNumber998 = "${userPhone.substring(0, 4)}"
        phoneNumberCode = "${userPhone.substring(4, 6)}"
        phoneNumberLast = "${userPhone.substring(11)}"
        binding.infoText.text =
            "Tekshiruv kodi quyidagi\nraqamga yuborildi\n$phoneNumber998 $phoneNumberCode *** ** $phoneNumberLast"
    }

    private val resendCodeObserver = Observer<Unit> {
        timber("RESEND CODE OBSERVER", "GGG")
        binding.waitingCodeTime.text = "Resend verification 1:00"
        isBreak = true
        startTime()

        binding.waitingCodeTime.apply {
            isClickable = false
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private val userPasswordObserver = Observer<String> { password ->
        userPassword = password
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

    private val currentPanObserver = Observer<String> { currentPan ->
        viewModel.verifyCard(
            VerifyCardRequest(
                currentPan,
                binding.verificationCodeEditText.text.toString()
            ),
            args.colorIndex
        )
    }

    private fun startTime() = binding.scope {
        waitingCodeTime.isClickable = false
        isBreak = false

        lifecycleScope.launch {
            minute = 60
            while (minute > 0) {
                if (isBreak) {
                    break
                }
                delay(1000)
                minute -= 1

                if (minute < 10) {
                    waitingCodeTime.text = "Resend verification 0:0$minute"
                } else {
                    waitingCodeTime.text = "Resend verification 0:$minute"
                }
            }
            if (minute == 0) {
                waitingCodeTime.apply {
                    isClickable = true
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.baseColor
                        )
                    )
                    setBackgroundResource(R.drawable.bg_username)
                    text = "Resend verification"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isBreak = true
    }
}