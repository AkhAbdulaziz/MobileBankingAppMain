package uz.gita.mobilebankingapp.presentation.ui.screens.card

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.VerifyCardRequest
import uz.gita.mobilebankingapp.databinding.ScreenVerifyCardBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.VerifyCardViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.VerifyCardViewModelImpl
import uz.gita.mobilebankingapp.utils.invisible
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class VerifyCardScreen : Fragment(R.layout.screen_verify_card) {
    private val binding by viewBinding(ScreenVerifyCardBinding::bind)
    private val viewModel: VerifyCardViewModel by viewModels<VerifyCardViewModelImpl>()
    private val args: VerifyCardScreenArgs by navArgs()

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
            progressBar.visible()
            viewModel.verifyCard(
                VerifyCardRequest(
                    viewModel.getCurrentPan(),
                    verificationCodeEditText.text.toString()
                ),
                args.colorIndex
            )
        }

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.exitScreenLiveData.observe(viewLifecycleOwner, exitScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private val exitScreenObserver = Observer<Unit> {
        binding.progressBar.invisible()
        findNavController().popBackStack()
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

}