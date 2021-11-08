package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.LogoutRequest
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.BasicViewModel
import javax.inject.Inject

@HiltViewModel
class BasicViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), BasicViewModel {
    override val openProfileScreenLiveData = MutableLiveData<Unit>()

    override fun openProfileScreen() {
        openProfileScreenLiveData.value = Unit
    }

    override fun getUserPhoneNumber(): String {
        return authRepository.getUserPhoneNumber()
    }

    override fun logout() {
        authRepository.logoutUser()
    }
}