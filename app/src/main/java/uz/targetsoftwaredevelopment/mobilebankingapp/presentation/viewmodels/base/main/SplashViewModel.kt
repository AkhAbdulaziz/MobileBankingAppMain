package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData

interface SplashViewModel {
    val openMainScreenLiveData: LiveData<Unit>
    val openAuthScreenLiveData: LiveData<Unit>
}