package uz.gita.mobilebankingapp.presentation.viewmodels.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.enum.StartScreenEnum
import uz.gita.mobilebankingapp.data.model.user_req_res.request.VerifyUserRequest
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.VerifyScreenViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class VerifyScreenViewModelImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val appRepository: AppRepository
) :
    ViewModel(), VerifyScreenViewModel {

    override val openMainScreenLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun userVerify(data: VerifyUserRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "Internet mavjud emas"
            return
        }

        authRepository.verifyUser(data).onEach {
            it.onFailure { throwable ->
                errorMessageLiveData.value = throwable.message
                appRepository.setStartScreen(StartScreenEnum.LOGIN)
            }
            it.onSuccess {
                openMainScreenLiveData.value = Unit
                appRepository.setStartScreen(StartScreenEnum.MAIN)
            }
        }.launchIn(viewModelScope)
    }

    override fun getUserPhoneNumber(): String {
        return authRepository.getUserPhoneNumber()
    }
}