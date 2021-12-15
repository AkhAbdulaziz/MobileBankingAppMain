package uz.gita.mobilebankingapp.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import uz.gita.mobilebankingapp.data.enum.StartScreenEnum
import java.io.File

fun Fragment.showToast(message: String) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
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
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    view.clearFocus()
}

fun File.toRequestData(): MultipartBody.Part {
    val requestFile = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("avatar", name, requestFile)
}