package uz.gita.mobilebankingapp.presentation.viewmodels.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.model.user_req_res.request.LoginRequest
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.LoginViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(private val repository: AuthRepository) : ViewModel(),
    LoginViewModel {
    override val enableLoginButtonLiveData = MutableLiveData<Unit>()
    override val disableLoginButtonLiveData = MutableLiveData<Unit>()
    override val showProgressLiveData = MutableLiveData<Unit>()
    override val hideProgressLiveData = MutableLiveData<Unit>()
    override val openVerifyScreenLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun userLogin(data: LoginRequest) {
        if (!isConnected()) {
            errorMessageLiveData.value = "Internet mavjud emas"
            return
        }

        disableLoginButtonLiveData.value = Unit
        showProgressLiveData.value = Unit
        repository.loginUser(data).onEach {
            it.onFailure { throwable ->
                enableLoginButtonLiveData.value = Unit
                errorMessageLiveData.value = throwable.message
                hideProgressLiveData.value = Unit
            }
            it.onSuccess {
                enableLoginButtonLiveData.value = Unit
                openVerifyScreenLiveData.value = Unit
                hideProgressLiveData.value = Unit
            }
        }.launchIn(viewModelScope)
    }
}