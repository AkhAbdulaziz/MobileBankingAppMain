package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.ProfileViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), ProfileViewModel {
    override val imageChangedLiveData = MutableLiveData<Uri>()
    override val profileInfoLiveData = MutableLiveData<ProfileInfoResponse>()
    override val userLocalDataLiveData = MutableLiveData<UserLocalData>()
    override val userLocalDataSavedLiveData = MutableLiveData<Unit>()


    init {
        authRepository.setUserLocalDataListener {
            userLocalDataSavedLiveData.value = Unit
        }
    }

    override fun setAvatar(imageUri: Uri) {
        imageChangedLiveData.value = imageUri
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
        authRepository.setUserLocalData(userLocalData)
    }
}