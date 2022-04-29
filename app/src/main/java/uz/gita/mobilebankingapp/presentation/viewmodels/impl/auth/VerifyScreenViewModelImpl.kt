package uz.gita.mobilebankingapp.presentation.viewmodels.impl.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.enums.StartScreenEnum
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.ResendRequest
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.VerifyUserRequest
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.auth.VerifyScreenViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class VerifyScreenViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val appRepository: AppRepository
) : ViewModel(), VerifyScreenViewModel {

    override val openMainScreenLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val resendCodeLiveData = MutableLiveData<Unit>()
    override val userPhoneNumberLiveData = MutableLiveData<String>()
    override val userPasswordLiveData = MutableLiveData<String>()

    override fun userVerify(data: VerifyUserRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.verifyUser(data).onEach {
                it.onSuccess {
                    openMainScreenLiveData.value = Unit
                    appRepository.setStartScreen(StartScreenEnum.MAIN)
                }
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                    appRepository.setStartScreen(StartScreenEnum.LOGIN)
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun resendCode(data: ResendRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.resend(data).onEach {
                it.onSuccess {
                    resendCodeLiveData.value = Unit
                }
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getUserPhoneNumber() {
        authRepository.getUserPhoneNumber().onEach {
            it.onSuccess {
                userPhoneNumberLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun getUserPassword() {
        userPasswordLiveData.value = authRepository.getUserPassword()
    }
}