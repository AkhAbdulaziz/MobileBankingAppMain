package uz.gita.mobilebankingapp.data.pref

import android.content.Context
import uz.gita.mobilebankingapp.data.enum.StartScreenEnum
import uz.gita.mobilebankingapp.utils.StringPreference
import uz.gita.mobilebankingapp.utils.startScreen

class MySharedPreferences private constructor(context: Context) {
    companion object {
        private lateinit var instance: MySharedPreferences
        fun init(context: Context) {
            instance = MySharedPreferences(context)
        }

        fun getPref(): MySharedPreferences = instance
    }

    private val pref = context.getSharedPreferences("MobileBankingApp", Context.MODE_PRIVATE)

    var userPhone: String by StringPreference(pref)

    var cardToken: String by StringPreference(pref)

    var userName: String by StringPreference(pref)

    var startScreen: StartScreenEnum
        set(value) = pref.edit().putString("startScreen", value.name).apply()
        get() = pref.getString("startScreen", StartScreenEnum.LOGIN.name)!!.startScreen()

    var accessToken: String by StringPreference(pref)

    var refreshToken: String by StringPreference(pref)

    var currentPan: String by StringPreference(pref)
}
