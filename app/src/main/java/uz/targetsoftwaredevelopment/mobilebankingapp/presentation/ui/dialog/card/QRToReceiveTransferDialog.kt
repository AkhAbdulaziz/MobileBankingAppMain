package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.card

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.DialogQrToReceiveTransferBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

@AndroidEntryPoint
class QRToReceiveTransferDialog(private val cardData: CardData) : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogQrToReceiveTransferBinding::bind)

    private var saveQRButtonClickListener: (() -> Unit)? = null
    fun setSaveQRButtonClickListener(f: () -> Unit) {
        saveQRButtonClickListener = f
    }

    private var shareQRButtonClickListener: (() -> Unit)? = null
    fun setShareQRButtonClickListener(f: () -> Unit) {
        shareQRButtonClickListener = f
    }

    private var printQRButtonClickListener: (() -> Unit)? = null
    fun setPrintQRButtonClickListener(f: () -> Unit) {
        printQRButtonClickListener = f
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
    ): View = inflater.inflate(R.layout.dialog_qr_to_receive_transfer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

        val writer = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix =
                writer.encode("${cardData.pan}", BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            imgQR.setImageBitmap(bmp)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        backBtn.setOnClickListener {
            dismiss()
        }

        btnSaveQR.setOnClickListener {
            saveQRButtonClickListener?.invoke()
        }

        btnShareQR.setOnClickListener {
            shareQRButtonClickListener?.invoke()
        }

        btnPrintQR.setOnClickListener {
            printQRButtonClickListener?.invoke()
        }
    }
}