package uz.gita.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.PaymePeopleViewModel
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