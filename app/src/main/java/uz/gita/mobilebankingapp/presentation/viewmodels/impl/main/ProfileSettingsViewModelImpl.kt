package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.ProfileSettingsViewModel
import uz.gita.mobilebankingapp.utils.timber
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ProfileSettingsViewModel, ViewModel() {

    override val profileInfoLiveData = MutableLiveData<ProfileInfoResponse>()
    override val userLocalDataLiveData = MutableLiveData<UserLocalData>()
    override val userLocalDataSavedLiveData = MutableLiveData<Unit>()

    init {
        authRepository.setUserLocalDataListener {
            userLocalDataSavedLiveData.value = Unit
        }
    }

    override fun getProfileInfo() {
        authRepository.getProfileInfo().onEach {
            it.onSuccess {
                profileInfoLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }

    override fun getUserLocalData() {
        userLocalDataLiveData.value = authRepository.getUserLocalData()
    }

    override fun setUserLocalData(userLocalData: UserLocalData) {
        timber("DataSaved ViewModel", "DATA_SAVED_00")
        authRepository.setUserLocalData(userLocalData)
    }
}