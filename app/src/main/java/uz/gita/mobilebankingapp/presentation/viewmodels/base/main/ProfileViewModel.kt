package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import android.net.Uri
import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse

interface ProfileViewModel {
    val imageChangedLiveData: LiveData<Uri>
    val profileInfoLiveData: LiveData<ProfileInfoResponse>
    val userLocalDataLiveData: LiveData<UserLocalData>
    val userLocalDataSavedLiveData : LiveData<Unit>
    val errorLiveData : LiveData<String>
    val openLoginScreenLiveData: LiveData<LogoutResponse>

    fun setAvatar(imageUri: Uri)

    fun getProfileInfo()

    fun getUserLocalData()

    fun setUserLocalData(userLocalData: UserLocalData)
}