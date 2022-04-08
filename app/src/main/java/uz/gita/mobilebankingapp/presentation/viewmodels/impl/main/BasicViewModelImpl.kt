package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.LogoutRequest
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.BasicViewModel
import javax.inject.Inject

@HiltViewModel
class BasicViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), BasicViewModel {
    override val openProfileScreenLiveData = MutableLiveData<Unit>()
    override val profileInfoLiveData = MutableLiveData<ProfileInfoResponse>()

    override fun openProfileScreen() {
        openProfileScreenLiveData.value = Unit
    }

    override fun getUserPhoneNumber(): String {
        return authRepository.getUserPhoneNumber()
    }

    override fun logout() {
        authRepository.logoutUser()
    }

    override fun getProfileInfo() {
        authRepository.getProfileInfo().onEach {
            it.onSuccess {
                profileInfoLiveData.value = it
            }
        }.launchIn(viewModelScope)
    }
}