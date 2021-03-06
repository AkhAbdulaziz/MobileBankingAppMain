package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.app.App
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import java.io.File
import java.util.*

fun Fragment.showToast(message: String) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showFancyToast(
    message: String,
    duration: Int = FancyToast.LENGTH_SHORT,
    toastType: Int = FancyToast.INFO
) {
    FancyToast.makeText(
        App.instance,
        message,
        toastType,
        duration,
        false
    ).show()
}

fun String.startScreen(): StartScreenEnum {
    return if (this == StartScreenEnum.LOGIN.name) StartScreenEnum.LOGIN
    else StartScreenEnum.MAIN
}

fun <T : ViewBinding> T.scope(block: T.() -> Unit) {
    block(this)
}

fun timber(message: String, tag: String = "TTT") {
    Timber.tag(tag).d(message)
}

fun Fragment.putArguments(block: Bundle.() -> Unit): Fragment {
    val bundle = arguments ?: Bundle()
    block(bundle)
    arguments = bundle
    return this
}

fun hideKeyboardFrom(context: Context, view: View) {
    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun File.toRequestData(): MultipartBody.Part {
    val requestFile = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("avatar", name, requestFile)
}

val cardBgImagesList = arrayOf(
    R.drawable.card_theme1,
    R.drawable.card_theme2,
    R.drawable.card_theme3,
    R.drawable.card_theme4,
    R.drawable.card_theme5,
    R.drawable.card_theme6,
    R.drawable.card_theme7,
    R.drawable.card_theme8,
    R.drawable.card_theme9,
    R.drawable.card_theme10,
    R.drawable.card_theme11,
    R.drawable.card_theme12,
    R.drawable.card_theme13,
    R.drawable.card_theme14,
    R.drawable.card_theme15,
    R.drawable.card_theme16
).toList()

val adsPrizesImagesList = arrayOf(
    R.drawable.ad_prizes1,
    R.drawable.ad_prizes2,
    R.drawable.ad_prizes1,
    R.drawable.ad_prizes2,
    R.drawable.ad_prizes1,
    R.drawable.ad_prizes2,
    R.drawable.ad_prizes1,
    R.drawable.ad_prizes2,
    R.drawable.ad_prizes1,
    R.drawable.ad_prizes2,
    R.drawable.ad_prizes1,
    R.drawable.ad_prizes2
).toList()

val months = arrayOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
)

fun getDate(timestamp: Long): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    val date = DateFormat.format("dd-MM-yyyy", calendar).toString()
    return date
}

fun vibratePhone(context: Context, timeInMilliSeconds: Long) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                timeInMilliSeconds,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        //deprecated in API 26
        vibrator.vibrate(timeInMilliSeconds)
    }
}