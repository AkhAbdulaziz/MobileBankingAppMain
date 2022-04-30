package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.auth

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.ResendRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.VerifyUserRequest

interface VerifyScreenViewModel {
    val openMainScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>
    val resendCodeLiveData: LiveData<Unit>
    val userPhoneNumberLiveData : LiveData<String>
    val userPasswordLiveData : LiveData<String>

    fun userVerify(data: VerifyUserRequest)

    fun getUserPhoneNumber()

    fun resendCode(data: ResendRequest)

    fun getUserPassword()
}