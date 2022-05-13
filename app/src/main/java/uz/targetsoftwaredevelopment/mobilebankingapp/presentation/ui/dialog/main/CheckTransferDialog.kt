package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.CheckData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.CheckDialogButtonsEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.DialogCheckTransferBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

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
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        innerCheckView.apply {
            txtReceiverCard.text = checkData.receiverPan
            txtReceiverName.text = checkData.receiverName
            txtTimeCreated.text = "${checkData.time}"
            txtTimePayment.text = "${checkData.time}"
            txtSenderCard.text = checkData.senderPan
            txtServiceCost.text = getPortableAmount("${checkData.fee.toInt()}")
            txtPaymentCost.text = getPortableAmount("${checkData.totalCost.toInt()}")
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

    private fun getPortableAmount(amount: String): String {
        var firstPiece = ""
        var secondPiece = ""
        var thirdPiece = ""

        if (amount.length <= 3) {
            firstPiece = amount
        } else if (amount.length <= 6) {
            secondPiece = amount.substring(amount.length - 3)
            firstPiece = amount.substring(0, amount.length - 3)
        } else if (amount.length <= 9) {
            thirdPiece = amount.substring(amount.length - 3)
            secondPiece = amount.substring(amount.length - 6, amount.length - 3)
            firstPiece = amount.substring(0, amount.length - 6)
        }

        return "$firstPiece $secondPiece $thirdPiece".trim()
    }
}
