package uz.gita.mobilebankingapp.utils

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import uz.gita.mobilebankingapp.R

class CheckInternetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.myFullscreenAlertDialogStyle)
        val layoutDialog: View =
            LayoutInflater.from(context).inflate(R.layout.screen_offline_mode, null)
        builder.setView(layoutDialog)

        val btnRepeat: AppCompatButton = layoutDialog.findViewById(R.id.btnRepeat)

        // Show dialog
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.window!!.setGravity(Gravity.CENTER)

        btnRepeat.setOnClickListener {
            checkConnection(dialog)
        }
        checkConnection(dialog)
    }

    private fun checkConnection(dialog: AlertDialog) {
        if (!isConnected()) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }
}
