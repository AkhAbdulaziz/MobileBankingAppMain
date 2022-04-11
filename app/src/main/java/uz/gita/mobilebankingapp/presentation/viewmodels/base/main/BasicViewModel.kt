package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse

interface BasicViewModel {

    val openProfileScreenLiveData: LiveData<Unit>
    val profileInfoLiveData: LiveData<ProfileInfoResponse>
    val errorMessageLiveData: LiveData<String>
    val openLoginScreenLiveData: LiveData<Unit>

    fun openProfileScreen()

    fun getUserPhoneNumber(): String

    fun logout()

    fun getProfileInfo()
}