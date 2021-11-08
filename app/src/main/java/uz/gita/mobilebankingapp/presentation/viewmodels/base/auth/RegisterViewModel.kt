package uz.gita.mobilebankingapp.presentation.viewmodels.base.auth

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.remote.user_req_res.request.RegisterRequest

interface RegisterViewModel {
    val enableRegisterLiveData : LiveData<Unit>
    val disableRegisterLiveData : LiveData<Unit>

    val progressLiveData : LiveData<Boolean>
    val errorLivaData : LiveData<String>
    val successLiveData : LiveData<String>

    fun registerUser(data : RegisterRequest)
}