package uz.gita.mobilebankingapp.presentation.viewmodels.base.auth

import androidx.lifecycle.LiveData

interface PinCodeScreenViewModel {
    val pinCodeLiveData: LiveData<String>
    val permissionFaceIDLiveData: LiveData<Boolean>
    val loginResponseLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>

    fun getPinCode()
    fun savePinCode(pinCode: String)
    fun getPermissionFaceID()
    fun loginUser()
}