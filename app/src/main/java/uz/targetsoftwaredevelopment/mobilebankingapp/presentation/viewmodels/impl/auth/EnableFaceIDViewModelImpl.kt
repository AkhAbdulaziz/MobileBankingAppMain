package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.auth.EnableFaceIDViewModel
import javax.inject.Inject

@HiltViewModel
class EnableFaceIDViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    EnableFaceIDViewModel, ViewModel() {

    override fun changeFaceIDPermission(permission: Boolean) {
        authRepository.changeFaceIDPermission(permission)
    }

    override fun changeConfirmationByFingerPrintPermission(permission: Boolean) {
        authRepository.changeConfirmationByFingerPrintPermission(permission)
    }
}