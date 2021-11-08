package uz.gita.mobilebankingapp.presentation.viewmodels.base.main

import androidx.lifecycle.LiveData

interface BasicViewModel {

    val openProfileScreenLiveData: LiveData<Unit>

    fun openProfileScreen()

    fun getUserPhoneNumber(): String

    fun logout()
}