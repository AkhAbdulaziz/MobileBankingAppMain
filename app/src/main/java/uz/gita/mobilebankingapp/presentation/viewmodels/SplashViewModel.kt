package uz.gita.mobilebankingapp.presentation.viewmodels

import androidx.lifecycle.LiveData

interface SplashViewModel {
    val openMainScreenLiveData: LiveData<Unit>
    val openAuthScreenLiveData: LiveData<Unit>
}