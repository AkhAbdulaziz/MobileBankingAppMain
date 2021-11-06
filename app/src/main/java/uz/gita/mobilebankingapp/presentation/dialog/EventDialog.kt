package uz.gita.mobilebankingapp.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R

@AndroidEntryPoint
class EventDialog : BottomSheetDialogFragment() {

    private var settingsListener: (() -> Unit)? = null
    fun setSettingsListener(f: () -> Unit) {
        settingsListener = f
    }

    private var deleteListener: (() -> Unit)? = null
    fun setDeleteListener(f: () -> Unit) {
        deleteListener = f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_event, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<View>(R.id.settingsCard).setOnClickListener {
            settingsListener?.invoke()
            dismiss()
        }
        view.findViewById<View>(R.id.deleteCard).setOnClickListener {
            deleteListener?.invoke()
            dismiss()
        }

        view.findViewById<View>(R.id.closeDialogBtn).setOnClickListener {
            dismiss()
        }
    }
}