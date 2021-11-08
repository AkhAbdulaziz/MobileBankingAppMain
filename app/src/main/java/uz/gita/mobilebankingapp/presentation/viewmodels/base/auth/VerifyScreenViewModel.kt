package uz.gita.mobilebankingapp.presentation.viewmodels.base.auth

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.VerifyUserRequest

interface VerifyScreenViewModel {
    val openMainScreenLiveData: LiveData<Unit>
    val errorMessageLiveData: LiveData<String>

    fun userVerify(data: VerifyUserRequest)

    fun getUserPhoneNumber(): String
}