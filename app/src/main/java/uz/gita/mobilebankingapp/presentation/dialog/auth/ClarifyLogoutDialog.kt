package uz.gita.mobilebankingapp.presentation.dialog.auth

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClarifyLogoutDialog : DialogFragment() {

    private var positiveBtnListener: (() -> Unit)? = null
    fun setPositiveBtnListener(block: () -> Unit) {
        positiveBtnListener = block
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Chindan ham ilovadan\nchiqishni hohlaysizmi?")
                .setPositiveButton("Ha",
                    DialogInterface.OnClickListener { dialog, id ->
                        positiveBtnListener?.invoke()
                    })
                .setNegativeButton("Bekor qilish",
                    DialogInterface.OnClickListener { dialog, id ->
                        dismiss()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}