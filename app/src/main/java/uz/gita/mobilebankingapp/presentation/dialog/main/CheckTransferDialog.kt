package uz.gita.mobilebankingapp.presentation.dialog.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.CheckData
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_check_transfer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

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