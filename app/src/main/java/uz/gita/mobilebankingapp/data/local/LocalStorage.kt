package uz.gita.mobilebankingapp.data.local

import android.content.Context
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.data.enums.StartScreenEnum
import uz.gita.mobilebankingapp.utils.BooleanPreference
import uz.gita.mobilebankingapp.utils.StringPreference
import uz.gita.mobilebankingapp.utils.startScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorage @Inject constructor() {
    private val pref = App.instance.getSharedPreferences("MobileBankingApp", Context.MODE_PRIVATE)

    var userPhone: String by StringPreference(pref)

    var userFullName: String by StringPreference(pref)

    var userPassword: String by StringPreference(pref)

    var startScreen: StartScreenEnum
        set(value) = pref.edit().putString("startScreen", value.name).apply()
        get() = pref.getString("startScreen", StartScreenEnum.LOGIN.name)!!.startScreen()

    var accessToken: String by StringPreference(pref)

    var refreshToken: String by StringPreference(pref)

    var currentPan: String by StringPreference(pref)

    var isBalanceVisible: Boolean by BooleanPreference(pref, true)

    var lastAllMoneyAmount: String by StringPreference(pref, "")

    var mainCardPan: String by StringPreference(pref)

    var userFirstName: String by StringPreference(pref)
    var userLastName: String by StringPreference(pref)
    var userNickName: String by StringPreference(pref)
    var userPhoneNumber1: String by StringPreference(pref)
    var userPhoneNumber2: String by StringPreference(pref)
    var userGender: String by StringPreference(pref)
    var userBirthday: String by StringPreference(pref)
}