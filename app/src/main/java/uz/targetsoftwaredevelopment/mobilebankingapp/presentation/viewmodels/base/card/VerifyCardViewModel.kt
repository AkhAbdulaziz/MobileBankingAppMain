package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.VerifyCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.request.ResendRequest

interface VerifyCardViewModel {
    val errorMessageLiveData: LiveData<String>
    val exitScreenLiveData: LiveData<Unit>
    val resendCodeLiveData: LiveData<Unit>
    val userPhoneNumberLiveData : LiveData<String>
    val userPasswordLiveData : LiveData<String>
    val currentPanLiveData : LiveData<String>

    fun verifyCard(data: VerifyCardRequest, bgColor: Int)

    fun getCurrentPan()

    fun getUserPhoneNumber()

    fun resendCode(data: ResendRequest)

    fun getUserPassword()
}