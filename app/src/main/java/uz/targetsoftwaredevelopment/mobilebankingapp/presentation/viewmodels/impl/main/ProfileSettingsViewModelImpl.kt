package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.UserLocalData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.request.UserInfoRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.ProfileSettingsViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.isConnected
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ProfileSettingsViewModel, ViewModel() {

    override val profileInfoLiveData = MutableLiveData<ProfileInfoResponse>()
    override val userLocalDataLiveData = MutableLiveData<UserLocalData>()
    override val userDataSavedLiveData = MutableLiveData<Unit>()
    override val errorLiveData = MutableLiveData<String>()
    override val openLoginScreenLiveData = MutableLiveData<LogoutResponse>()

    init {
        authRepository.setUserDataSavedListener {
            userDataSavedLiveData.value = Unit
        }
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(LogoutResponse("LogoutCauseInternetError"))
        }
    }

    override fun getProfileInfo() {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.getProfileInfo().onEach {
                it.onSuccess {
                    profileInfoLiveData.value = it
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun getUserLocalData() {
        authRepository.getUserLocalData().onEach {
            it.onSuccess {
                userLocalDataLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun saveUserData(userLocalData: UserLocalData) {
        if (!isConnected()) {
            errorLiveData.value = "${R.string.no_internet}"
        } else {
            authRepository.putUserInfo(
                UserInfoRequest(
                    userLocalData.firstName,
                    userLocalData.lastName
                )
            ).onEach {
                it.onSuccess {
                    authRepository.saveUserData(userLocalData)
                }
            }.launchIn(viewModelScope)
        }
    }
}