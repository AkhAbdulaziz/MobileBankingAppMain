package uz.gita.mobilebankingapp.presentation.viewmodels.impl.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.RegisterRequest
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.auth.RegisterViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(private val repository: AuthRepository) :
    ViewModel(), RegisterViewModel {

    override val enableRegisterLiveData = MutableLiveData<Unit>()
    override val disableRegisterLiveData = MutableLiveData<Unit>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val errorLivaData = MutableLiveData<String>()
    override val successLiveData = MutableLiveData<String>()

    override fun registerUser(data: RegisterRequest) {
        if (!isConnected()) {
            errorLivaData.value = "${R.string.no_internet}"
        } else {
            progressLiveData.value = true
            disableRegisterLiveData.value = Unit
            repository.registerUser(data).onEach {
                progressLiveData.value = false
                enableRegisterLiveData.value = Unit
                it.onFailure { throwable ->
                    errorLivaData.value = throwable.message
                }
                it.onSuccess { message ->
                    successLiveData.value = message
                }
            }.launchIn(viewModelScope)
        }
    }
}