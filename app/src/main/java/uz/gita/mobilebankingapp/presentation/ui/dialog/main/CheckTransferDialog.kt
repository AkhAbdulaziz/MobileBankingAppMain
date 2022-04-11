package uz.gita.mobilebankingapp.presentation.ui.dialog.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.CheckData
import uz.gita.mobilebankingapp.data.enums.CheckDialogButtonsEnum
import uz.gita.mobilebankingapp.databinding.DialogCheckTransferBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class CheckTransferDialog(private val checkData: CheckData) : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogCheckTransferBinding::bind)

    private var shareBtnListener: ((CheckDialogButtonsEnum) -> Unit)? = null
    fun setShareBtnListener(block: (CheckDialogButtonsEnum) -> Unit) {
        shareBtnListener = block
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
    ): View {
        return inflater.inflate(R.layout.dialog_check_transfer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        /*  dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentDialogStyle)*/

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        innerCheckView.apply {
            txtReceiverCard.text = checkData.receiverPan
            txtReceiverName.text = checkData.receiverName
            txtTimeCreated.text = "${checkData.time}"
            txtTimePayment.text = "${checkData.time}"
            txtSenderCard.text = checkData.senderPan
            txtServiceCost.text = "${checkData.fee}"
            txtPaymentCost.text = "${checkData.totalCost}"
        }

        btnDownload.setOnClickListener {
            shareBtnListener?.invoke(CheckDialogButtonsEnum.DOWNLOAD)
        }

        btnShare.setOnClickListener {
            shareBtnListener?.invoke(CheckDialogButtonsEnum.SHARE)
        }

        btnPrintQR.setOnClickListener {
            shareBtnListener?.invoke(CheckDialogButtonsEnum.PRINT_QR)
        }
    }
}
