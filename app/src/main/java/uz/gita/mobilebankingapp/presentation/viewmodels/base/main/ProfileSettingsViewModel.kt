package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse

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