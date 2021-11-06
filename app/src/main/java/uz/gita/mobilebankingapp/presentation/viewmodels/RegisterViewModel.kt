package uz.gita.mobilebankingapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import uz.gita.mobilebankingapp.data.model.user_req_res.request.RegisterRequest

interface RegisterViewModel {
    val enableRegisterLiveData : LiveData<Unit>
    val disableRegisterLiveData : LiveData<Unit>

    val progressLiveData : LiveData<Boolean>
    val errorLivaData : LiveData<String>
    val successLiveData : LiveData<String>

    fun registerUser(data : RegisterRequest)
}