package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse

interface PaymePeopleViewModel {
    val openLoginScreenLiveData: LiveData<LogoutResponse>
}