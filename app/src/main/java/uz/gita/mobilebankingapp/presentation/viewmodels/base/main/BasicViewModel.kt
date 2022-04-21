package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse

interface BasicViewModel {
    val openProfileScreenLiveData: LiveData<Unit>
    val profileInfoLiveData: LiveData<ProfileInfoResponse>
    val errorMessageLiveData: LiveData<String>
    val openLoginScreenLiveData: LiveData<LogoutResponse>
    val logoutResponseLiveData: LiveData<LogoutResponse>

    fun openProfileScreen()

    fun getUserPhoneNumber(): String

    fun logout()

    fun getProfileInfo()
}