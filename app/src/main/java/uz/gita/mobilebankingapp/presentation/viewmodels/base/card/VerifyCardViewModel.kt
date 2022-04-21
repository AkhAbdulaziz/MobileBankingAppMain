package uz.gita.mobilebankingapp.presentation.viewmodels.base.card

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.VerifyCardRequest
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.ResendRequest

interface VerifyCardViewModel {
    val errorMessageLiveData: LiveData<String>
    val exitScreenLiveData: LiveData<Unit>
    val resendCodeLiveData: LiveData<Unit>
    val userPhoneNumberLiveData : LiveData<String>
    val userPasswordLiveData : LiveData<String>

    fun verifyCard(data: VerifyCardRequest, bgColor: Int)

    fun getCurrentPan() : String

    fun getUserPhoneNumber()

    fun resendCode(data: ResendRequest)

    fun getUserPassword()
}