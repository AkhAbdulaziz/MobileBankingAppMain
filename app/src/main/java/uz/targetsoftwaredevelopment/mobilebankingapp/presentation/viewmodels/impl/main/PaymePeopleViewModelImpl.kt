package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.PaymePeopleViewModel
import javax.inject.Inject

@HiltViewModel
class PaymePeopleViewModelImpl @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel(), PaymePeopleViewModel {
    override val openLoginScreenLiveData = MutableLiveData<LogoutResponse>()

    init {
        authRepository.setOpenLoginScreenListener {
            openLoginScreenLiveData.postValue(LogoutResponse("LogoutCauseInternetError"))
        }
    }
}