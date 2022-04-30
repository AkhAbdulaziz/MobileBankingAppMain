package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse

interface BasicViewModel {
    val openProfileScreenLiveData: LiveData<Unit>
    val profileInfoLiveData: LiveData<ProfileInfoResponse>
    val errorMessageLiveData: LiveData<String>
    val openLoginScreenLiveData: LiveData<LogoutResponse>
    val logoutResponseLiveData: LiveData<LogoutResponse>
    val userPhoneNumberLiveData: LiveData<String>

    fun openProfileScreen()

    fun getUserPhoneNumber()

    fun logout()

    fun getProfileInfo()

    fun givePanToTransferPage(pan: String)
}