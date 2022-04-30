package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.card

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.DialogEventBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

@AndroidEntryPoint
class EventDialog(private val cardData: CardData) : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogEventBinding::bind)

    private var settingsListener: (() -> Unit)? = null
    fun setSettingsListener(f: () -> Unit) {
        settingsListener = f
    }

    private var deleteListener: (() -> Unit)? = null
    fun setDeleteListener(f: () -> Unit) {
        deleteListener = f
    }

    private var shareQRListener: (() -> Unit)? = null
    fun setShareQRListener(f: () -> Unit) {
        shareQRListener = f
    }

    override fun onStart() {
        super.onStart()
        //this forces the sheet to appear at max height even on landscape
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_event, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

        btnShareQr.setOnClickListener {
            shareQRListener?.invoke()
        }

        btnShareLink.setOnClickListener {
            /*val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val link =
                "Pay quickly!: https://play.google.com/store/apps/details?id=${App.instance.packageName}"
            intent.putExtra(Intent.EXTRA_TEXT, link)
            requireActivity().startActivity(Intent.createChooser(intent, "Share:"))*/

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "*/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, "Card number: ${cardData.pan}")
            startActivity(Intent.createChooser(sharingIntent, "Check PDF"))
        }

        settingsCard.setOnClickListener {
            settingsListener?.invoke()
            dismiss()
        }
        deleteCard.setOnClickListener {
            deleteListener?.invoke()
            dismiss()
        }

        closeDialogBtn.setOnClickListener {
            dismiss()
        }
    }
}