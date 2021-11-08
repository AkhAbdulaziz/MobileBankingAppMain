package uz.gita.mobilebankingapp.presentation.viewmodels.base.auth

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.LoginRequest

interface LoginViewModel {
    val enableLoginButtonLiveData: LiveData<Unit>
    val disableLoginButtonLiveData: LiveData<Unit>

    val showProgressLiveData: LiveData<Unit>
    val hideProgressLiveData: LiveData<Unit>

    val openVerifyScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>

    fun userLogin(data: LoginRequest)
}