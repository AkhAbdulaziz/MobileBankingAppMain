package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import android.app.Activity
import android.util.Log
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.app.App

fun errorFlashBar(activity: Activity, title: String, message: String, colorResID: Int): Flashbar {

    Log.d("TOKEN_ERROR", "In Util")
    return Flashbar.Builder(activity)
        .gravity(Flashbar.Gravity.BOTTOM)
        .title(title)
        .titleSizeInSp(18f)
        .message(message)
        .messageSizeInSp(16f)
        .showIcon()
        .duration(3000)
        .icon(R.drawable.ic_alert)
        .iconColorFilterRes(R.color.white)
        .backgroundColorRes(colorResID)
        .enterAnimation(
            FlashAnim.with(App.instance)
                .animateBar()
                .duration(750)
                .alpha()
                .overshoot()
        )
        .exitAnimation(
            FlashAnim.with(App.instance)
                .animateBar()
                .duration(400)
                .accelerateDecelerate()
        )
        .build()
}
