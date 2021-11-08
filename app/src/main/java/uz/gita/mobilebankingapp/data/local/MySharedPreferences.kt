package uz.gita.mobilebankingapp.data.local

import android.content.Context
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.data.enum.StartScreenEnum
import uz.gita.mobilebankingapp.utils.StringPreference
import uz.gita.mobilebankingapp.utils.startScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MySharedPreferences @Inject constructor() {
    private val pref = App.instance.getSharedPreferences("MobileBankingApp", Context.MODE_PRIVATE)

    var userPhone: String by StringPreference(pref)

    var cardToken: String by StringPreference(pref)

    var userFullName: String by StringPreference(pref)

    var startScreen: StartScreenEnum
        set(value) = pref.edit().putString("startScreen", value.name).apply()
        get() = pref.getString("startScreen", StartScreenEnum.LOGIN.name)!!.startScreen()

    var accessToken: String by StringPreference(pref)

    var refreshToken: String by StringPreference(pref)

    var currentPan: String by StringPreference(pref)
}
