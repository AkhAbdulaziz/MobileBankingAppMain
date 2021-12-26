package uz.gita.mobilebankingapp.domain.repository

import uz.gita.mobilebankingapp.data.enums.StartScreenEnum

interface AppRepository {
    fun startScreen(): StartScreenEnum

    fun setStartScreen(startScreenEnum: StartScreenEnum)

    fun getToken() : String
}