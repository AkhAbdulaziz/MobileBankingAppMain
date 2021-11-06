package uz.gita.mobilebankingapp.domain.repository

import uz.gita.mobilebankingapp.data.enum.StartScreenEnum

interface AppRepository {
    fun startScreen(): StartScreenEnum

    fun setStartScreen(startScreenEnum: StartScreenEnum)

    fun getToken() : String
}