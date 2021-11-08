package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.ProfileViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModelImpl @Inject constructor(private val repository: AuthRepository) :
    ViewModel(), ProfileViewModel {
    override val imageChangedLiveData = MutableLiveData<Uri>()

    override fun setAvatar(imageUri: Uri) {
        imageChangedLiveData.value = imageUri
    }
}