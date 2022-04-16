package uz.gita.mobilebankingapp.domain.repository.impl

import uz.gita.mobilebankingapp.data.enums.StartScreenEnum
import uz.gita.mobilebankingapp.data.local.LocalStorage
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val pref: LocalStorage) : AppRepository {

    override fun startScreen(): StartScreenEnum = pref.startScreen
    override fun setStartScreen(startScreenEnum: StartScreenEnum) {
        pref.startScreen = startScreenEnum
    }

    override fun getToken(): String = pref.accessToken

}