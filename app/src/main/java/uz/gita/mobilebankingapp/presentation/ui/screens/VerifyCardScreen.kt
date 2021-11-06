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
import uz.gita.mobilebankingapp.data.model.card_req_res.request.VerifyCardRequest
import uz.gita.mobilebankingapp.databinding.ScreenVerifyBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.VerifyCardViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.VerifyCardViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class VerifyCardScreen : Fragment(R.layout.screen_verify_card) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyCardViewModel by viewModels<VerifyCardViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        sendBtn.setOnClickListener {
            viewModel.verifyCard(
                VerifyCardRequest(
                    viewModel.getCurrentPan(),
                    verificationCodeEditText.text.toString()
                )
            )
        }

        viewModel.openMyCardsScreenLiveData.observe(viewLifecycleOwner, openMyCardsScreenObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    private val openMyCardsScreenObserver = Observer<Unit> {
//        findNavController().navigate(R.id.action_verifyCardScreen_to_myCardsScreen)
        findNavController().popBackStack()
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

}