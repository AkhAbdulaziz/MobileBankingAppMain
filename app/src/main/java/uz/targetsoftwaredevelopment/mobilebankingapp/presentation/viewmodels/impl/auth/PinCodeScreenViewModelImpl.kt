package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.auth.PinCodeScreenViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.timber
import javax.inject.Inject

@HiltViewModel
class PinCodeScreenViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), PinCodeScreenViewModel {
    override val pinCodeLiveData = MutableLiveData<String>()
    override val permissionFaceIDLiveData = MutableLiveData<Boolean>()
    override val loginResponseLiveData = MutableLiveData<Unit>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun getPermissionFaceID() {
        authRepository.getFaceIDPermission().onEach {
            it.onSuccess {
                permissionFaceIDLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun getPinCode() {
        authRepository.getPinCode().onEach {
            it.onSuccess {
                pinCodeLiveData.value = it
            }
        }.launchIn(viewModelScope)
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