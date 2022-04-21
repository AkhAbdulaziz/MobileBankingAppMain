package uz.gita.mobilebankingapp.presentation.viewmodels.impl.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.auth.PinCodeScreenViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import uz.gita.mobilebankingapp.utils.timber
import javax.inject.Inject

@HiltViewModel
class PinCodeScreenViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), PinCodeScreenViewModel {
    override val pinCodeLiveData = MutableLiveData<String>()
    override val permissionFaceIDLiveData = MutableLiveData<Boolean>()
    override val loginResponseLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun getPermissionFaceID() {
        permissionFaceIDLiveData.value = authRepository.getFaceIDPermission()
    }

    override fun getPinCode() {
        pinCodeLiveData.value = authRepository.getPinCode()
    }

    override fun savePinCode(pinCode: String) {
        authRepository.savePinCode(pinCode)
    }

    override fun loginUser() {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.loginUser().onEach {
                it.onFailure { throwable ->
                    errorMessageLiveData.value = throwable.message
                }
                it.onSuccess {
                    loginResponseLiveData.value = Unit
                }
            }.catch { throwable ->
                timber("${throwable.message}")
            }.launchIn(viewModelScope)
        }
    }
}