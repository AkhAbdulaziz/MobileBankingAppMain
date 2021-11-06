package uz.gita.mobilebankingapp.domain.repository.impl

import uz.gita.mobilebankingapp.data.enum.StartScreenEnum
import uz.gita.mobilebankingapp.data.pref.MySharedPreferences
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor() : AppRepository {
    private val pref = MySharedPreferences.getPref()

    override fun startScreen(): StartScreenEnum = pref.startScreen
    override fun setStartScreen(startScreenEnum: StartScreenEnum) {
        pref.startScreen = startScreenEnum
    }
    override fun getToken(): String = pref.accessToken

}