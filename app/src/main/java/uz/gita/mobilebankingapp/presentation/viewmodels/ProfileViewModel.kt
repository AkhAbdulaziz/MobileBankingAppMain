package uz.gita.mobilebankingapp.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData

interface ProfileViewModel {
    val imageChangedLiveData: LiveData<Uri>

    fun setAvatar(imageUri : Uri)
}