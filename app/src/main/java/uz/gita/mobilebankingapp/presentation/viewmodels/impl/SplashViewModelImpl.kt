package uz.gita.mobilebankingapp.presentation.viewmodels.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.mobilebankingapp.data.enum.StartScreenEnum
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import uz.gita.mobilebankingapp.presentation.viewmodels.SplashViewModel
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