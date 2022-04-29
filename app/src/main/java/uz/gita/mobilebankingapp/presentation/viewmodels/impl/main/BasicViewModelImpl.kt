package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.BasicViewModel
import uz.gita.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class BasicViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), BasicViewModel {
    override val openProfileScreenLiveData = MutableLiveData<Unit>()
    override val profileInfoLiveData = MutableLiveData<ProfileInfoResponse>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val openLoginScreenLiveData = MutableLiveData<LogoutResponse>()
    override val logoutResponseLiveData = MutableLiveData<LogoutResponse>()
    override val userPhoneNumberLiveData = MutableLiveData<String>()

    init {
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(LogoutResponse("LogoutCauseInternetError"))
        }
    }

    override fun openProfileScreen() {
        openProfileScreenLiveData.value = Unit
    }

    override fun getUserPhoneNumber() {
        authRepository.getUserPhoneNumber().onEach {
            it.onSuccess {
                userPhoneNumberLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun logout() {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.logoutUser().onEach {
                it.onSuccess {
                    logoutResponseLiveData.value = it
                }
                it.onFailure {
                    errorMessageLiveData.value = it.message
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getProfileInfo() {
        if (!isConnected()) {
            errorMessageLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.getProfileInfo().onEach {
                it.onSuccess {
                    profileInfoLiveData.value = it
                }
                it.onFailure {
                    errorMessageLiveData.value = it.message
                }
            }.launchIn(viewModelScope)
        }
    }
}