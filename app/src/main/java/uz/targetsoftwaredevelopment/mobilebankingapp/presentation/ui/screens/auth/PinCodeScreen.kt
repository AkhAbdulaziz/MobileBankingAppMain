package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrognito.flashbar.Flashbar
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenPinCodeBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.auth.PinCodeScreenViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.auth.PinCodeScreenViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.*
import java.util.concurrent.Executor

@AndroidEntryPoint
class PinCodeScreen : Fragment(R.layout.screen_pin_code) {
    private val binding by viewBinding(ScreenPinCodeBinding::bind)
    private val viewModel: PinCodeScreenViewModel by viewModels<PinCodeScreenViewModelImpl>()
    private val args: PinCodeScreenArgs by navArgs()
    private var flashbar: Flashbar? = null

    private var firstCircles = 0
    private var firstPinCode = ""
    private var circles = 0
    private var pinCode = ""
    private var firstTime: Boolean = true
    private var savedPinCode = ""

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        if (args.needLoginToRefreshToken) {
            if (flashbar == null) {
                flashbar = errorFlashBar(
                    requireActivity(),
                    "Error",
                    "Access denied, re-enter.",
                    R.color.colorErrorRed
                )
            }
            flashbar?.show()
        }
        viewModel.getPinCode()

        viewModel.permissionFaceIDLiveData.observe(viewLifecycleOwner, permissionFaceIDObserver)
        viewModel.pinCodeLiveData.observe(viewLifecycleOwner, pinCodeObserver)
        viewModel.loginResponseLiveData.observe(viewLifecycleOwner, loginResponseObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)

        if (biometricAvailable()) {
            viewModel.getPermissionFaceID()

            innerPincodesView.btnBiometricAuth.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
        } else {
            innerPincodesView.btnBiometricAuth.invisible()
        }

        if (args.lastScreen == StartScreenEnum.LOGIN) {
            tvEnterPassword.invisible()
            innerPincodesView.btnBiometricAuth.invisible()
            tvTitle.text = "Set PIN"
        } else {
            tvEnterPassword.visible()
            innerPincodesView.btnBiometricAuth.visible()
            tvTitle.text = "Enter PIN"
        }

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(
            this@PinCodeScreen,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                /*override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }*/

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.loginUser()
                }

                /* override fun onAuthenticationFailed() {
                     super.onAuthenticationFailed()
                     Toast.makeText(
                         requireContext(,
                         "Authentication failed!",
                         Toast.LENGTH_SHORT
                     ).show()
                 }*/
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credentials")
            .setNegativeButtonText("Use account password")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()

        tvEnterPassword.setOnClickListener {
            findNavController().navigate(PinCodeScreenDirections.actionPinCodeScreenToLoginScreen())
        }

        setInnerPinCodeViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setInnerPinCodeViews() = binding.scope {
        innerPincodesView.apply {

            btn1.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "1"
                            circles++
                        }
                        changeCirclesColor()

                        txt1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn2.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "2"
                            circles++
                        }
                        changeCirclesColor()

                        txt2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn3.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "3"
                            circles++
                        }
                        changeCirclesColor()

                        txt3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn4.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt4.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt4.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "4"
                            circles++
                        }
                        changeCirclesColor()

                        txt4.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn5.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt5.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt5.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "5"
                            circles++
                        }
                        changeCirclesColor()

                        txt5.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn6.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt6.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false
                        txt6.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "6"
                            circles++
                        }
                        changeCirclesColor()

                        txt6.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn7.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt7.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt7.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "7"
                            circles++
                        }
                        changeCirclesColor()

                        txt7.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn8.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt8.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt8.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "8"
                            circles++
                        }
                        changeCirclesColor()

                        txt8.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn9.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt9.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt9.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "9"
                            circles++
                        }
                        changeCirclesColor()

                        txt9.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btn0.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        txt0.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.isPressed = false

                        txt0.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        this.isPressed = true

                        if (circles < 4) {
                            pinCode += "0"
                            circles++
                        }
                        changeCirclesColor()

                        txt0.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    true
                }
            }

            btnDelete.apply {
                setOnTouchListener { view, motionEvent ->
                    val action = motionEvent.action

                    if (action == MotionEvent.ACTION_MOVE) {
                        this.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_UP) {
                        this.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyColor
                            )
                        )
                    }
                    if (action == MotionEvent.ACTION_DOWN) {
                        if (circles > 0) {
                            pinCode.substring(circles - 1)
                            circles--
                        }
                        changeCirclesColor()

                        this.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.greyBg
                            )
                        )
                    }
                    true
                }
            }
        }
    }

    private val permissionFaceIDObserver = Observer<Boolean> { isAllowed ->
        if (isAllowed) {
            binding.innerPincodesView.btnBiometricAuth.visible()
            biometricPrompt.authenticate(promptInfo)
        } else {
            binding.innerPincodesView.btnBiometricAuth.invisible()
        }
    }

    private val pinCodeObserver = Observer<String> {
        savedPinCode = it
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        binding.progressBar.gone()
        showToast(errorMessage)
    }

    private val loginResponseObserver = Observer<Unit> {
        binding.progressBar.gone()
        findNavController().navigate(PinCodeScreenDirections.actionPinCodeScreenToBasicScreen(args.isNewUser))
    }

    // Biometric Strong is FingerPrint
    // Biometric Weak is Face ID
    private fun biometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(requireContext())
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("MY_APP_TAG", "NO biometric features available on this device.")
                true
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("MY_APP_TAG", "Biometric features are currently not available.")
                true
            }
            else -> {
                Log.d("MY_APP_TAG", "Last false")
                false
            }
        }
    }

    private fun changeCirclesColor() = binding.scope {
        when (circles) {
            0 -> {
                clearCode()
            }
            1 -> {
                circle1.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle2.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pin_circle_default_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle3.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pin_circle_default_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle4.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pin_circle_default_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            2 -> {
                circle1.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle2.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle3.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pin_circle_default_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle4.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pin_circle_default_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            3 -> {
                circle1.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle2.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle3.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle4.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pin_circle_default_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            4 -> {
                circle1.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle2.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle3.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                circle4.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightBaseColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                checkCode()
            }
        }
    }

    private fun checkCode() = binding.scope {
        if (args.lastScreen == StartScreenEnum.LOGIN) {
            if (firstTime) {
                firstTime = false
                firstCircles = circles
                firstPinCode = pinCode
                circles = 0
                pinCode = ""
                tvTitle.text = "Confirm new PIN code"
                clearCode()
            } else {
                progressBar.visible()
                if (firstPinCode == pinCode) {
                    viewModel.savePinCode(pinCode)
                    progressBar.gone()
                    findNavController().navigate(
                        PinCodeScreenDirections.actionPinCodeScreenToEnableFaceIDScreen(
                            args.isNewUser
                        )
                    )
                } else {
                    progressBar.gone()
                    clearCode()
                    vibratePhone(requireContext(), 200)
                    tvTitle.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorErrorRed
                        )
                    )
                }
            }
        } else {
            progressBar.visible()
            if (savedPinCode == pinCode) {
                viewModel.loginUser()
            } else {
                progressBar.gone()
                clearCode()
                vibratePhone(requireContext(), 200)
                tvTitle.apply {
                    text = "Invalid PIN code"
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorErrorRed
                        )
                    )
                }
            }
        }
    }

    private fun clearCode() = binding.scope {
        circle1.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.pin_circle_default_color
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
        circle2.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.pin_circle_default_color
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
        circle3.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.pin_circle_default_color
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
        circle4.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.pin_circle_default_color
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
        circles = 0
        pinCode = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        flashbar = null
    }
}
