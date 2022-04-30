package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout


fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.requireContext(), message, duration).show()
}

fun View.visible(bool: Boolean) {
    if (bool) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.setMarginsInDp(left: Float, top: Float, right: Float, bottom: Float) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left.toPx(), top.toPx(), right.toPx(), bottom.toPx())
        this.requestLayout()
    }
}

fun TextInputLayout.enableError() {
    this.isErrorEnabled = true
}

fun TextInputLayout.disableError() {
    this.isErrorEnabled = false
}
