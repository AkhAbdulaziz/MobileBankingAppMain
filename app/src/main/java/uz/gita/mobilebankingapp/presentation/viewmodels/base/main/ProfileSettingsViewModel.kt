package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse

interface ProfileSettingsViewModel {
    val profileInfoLiveData: LiveData<ProfileInfoResponse>
    val userLocalDataLiveData: LiveData<UserLocalData>
    val userLocalDataSavedLiveData : LiveData<Unit>

    fun getProfileInfo()

    fun getUserLocalData()

    fun setUserLocalData(userLocalData: UserLocalData)
}