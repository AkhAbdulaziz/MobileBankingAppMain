package uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository

import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum

interface AppRepository {
    fun startScreen(): StartScreenEnum

    fun setStartScreen(startScreenEnum: StartScreenEnum)

    fun getToken() : String
}