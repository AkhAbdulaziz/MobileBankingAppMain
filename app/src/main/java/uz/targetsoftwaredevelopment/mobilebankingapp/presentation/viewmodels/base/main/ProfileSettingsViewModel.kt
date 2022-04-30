package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.UserLocalData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse

interface ProfileSettingsViewModel {
    val profileInfoLiveData: LiveData<ProfileInfoResponse>
    val userLocalDataLiveData: LiveData<UserLocalData>
    val userDataSavedLiveData : LiveData<Unit>
    val errorLiveData : LiveData<String>
    val openLoginScreenLiveData: LiveData<LogoutResponse>

    fun getProfileInfo()

    fun getUserLocalData()

    fun saveUserData(userLocalData: UserLocalData)
}