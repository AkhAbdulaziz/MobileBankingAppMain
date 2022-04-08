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
        if (!isConnected()) {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            val layoutDialog: View =
                LayoutInflater.from(context).inflate(R.layout.screen_offline_mode, null)
            builder.setView(layoutDialog)

            val btnRepeat: AppCompatButton = layoutDialog.findViewById(R.id.btnRepeat)

            // Show dialog
            val dialog: AlertDialog = builder.create()
            dialog.show()
            dialog.setCancelable(false)

            dialog.window!!.setGravity(Gravity.CENTER)

            btnRepeat.setOnClickListener {
                dialog.dismiss()
                onReceive(context, intent)
            }
        }
    }
}
