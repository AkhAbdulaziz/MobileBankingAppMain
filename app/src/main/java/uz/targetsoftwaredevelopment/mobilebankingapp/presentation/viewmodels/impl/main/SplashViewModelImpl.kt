package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AppRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.SplashViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(private val repository: AppRepository) :
    ViewModel(), SplashViewModel {

    override val openMainScreenLiveData = MutableLiveData<Unit>()
    override val openAuthScreenLiveData = MutableLiveData<Unit>()

    init {
        viewModelScope.launch {
            delay(2000)
            if (repository.startScreen() == StartScreenEnum.MAIN) {
                openMainScreenLiveData.value = Unit
            } else {
                openAuthScreenLiveData.value = Unit
            }
        }
    }
}