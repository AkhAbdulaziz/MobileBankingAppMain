package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.CheckData
import uz.gita.mobilebankingapp.data.enums.CheckDialogButtonsEnum
import uz.gita.mobilebankingapp.databinding.ScreenCheckTransferBinding
import uz.gita.mobilebankingapp.presentation.ui.dialog.main.CheckTransferDialog
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast
import java.io.*


class CheckTransferScreen : Fragment(R.layout.screen_check_transfer) {
    private val binding by viewBinding(ScreenCheckTransferBinding::bind)
    private val args: CheckTransferScreenArgs by navArgs()
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        receiverPan.text = args.checkData.receiverPan
        receiverName.text = args.checkData.receiverName
        costService.text = "${args.checkData.fee} so'm"
        senderPan.text = args.checkData.senderPan
        amountPayment.text = "${args.checkData.totalCost}"
        dateAndTime.text = "${args.checkData.time}"

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        bottomBar.imgRetry.setOnClickListener {
            showToast("Retry img pressed")
        }

        bottomBar.imgSave.setOnClickListener {
            showToast("Save img pressed")
        }

        bottomBar.imgCheck.setOnClickListener {
            val checkDialog = CheckTransferDialog(args.checkData)
            checkDialog.setShareBtnListener { buttonType ->
                when (buttonType) {
                    CheckDialogButtonsEnum.DOWNLOAD -> {
                        showToast("Download button pressed")
                    }
                    CheckDialogButtonsEnum.SHARE -> {
                        verifyStoragePermissions()
                        try {
                            createPdf(
                                args.checkData,
                                R.drawable.infinbank_logo,
                                "Invest-Finance Bank"
                            )
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                    CheckDialogButtonsEnum.PRINT_QR -> {
                        showToast("Print QR button pressed")
                    }
                }
            }
            checkDialog.isCancelable = true
            checkDialog.show(childFragmentManager, "check_dialog")
        }

        bottomBar.imgCancel.setOnClickListener {
            showToast("Cancel img pressed")
        }
    }

    private fun verifyStoragePermissions() {
        // Check if we have write permission
        val permission: Int =
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                1
            )
        }
    }

    @Throws(FileNotFoundException::class)
    private fun createPdf(
        checkData: CheckData,
        imgIDReceiverCompany: Int,
        nameReceiverCompany: String
    ) {
        val pdfPath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
        val file = File(pdfPath, "receiptPdf.pdf")
        val outputStream: OutputStream = FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        pdfDocument.defaultPageSize = PageSize(300f, 290f)
        document.setMargins(8f, 8f, 8f, 8f)

        val imgLogoPAYME = getPDFImage(R.drawable.payme_logo)
            .scaleToFit(80f, 800f)

        val imgMiniLogoPAYME = getPDFImage(R.drawable.payme_logo)
            .scaleToFit(30f, 130f)

        val imgLogoReceiverCompany = getPDFImage(imgIDReceiverCompany)
            .scaleToFit(90f, 850f)

        val columnWidth = floatArrayOf(160f, 160f)
        val table = Table(columnWidth)
        table.setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE)

        val innerColumnWidth = floatArrayOf(50f, 50f)
        val innerTable = Table(innerColumnWidth)
        innerTable.setBorder(Border.NO_BORDER).setHorizontalAlignment(HorizontalAlignment.RIGHT)

        innerTable.addCell(
            Cell().add(
                Paragraph("Powered by").setFontColor(ColorConstants.LIGHT_GRAY).setFontSize(8f)
            ).setBorder(Border.NO_BORDER)
        )
        innerTable.addCell(
            Cell().add(imgMiniLogoPAYME)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(imgLogoPAYME)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(imgLogoReceiverCompany.setHorizontalAlignment(HorizontalAlignment.RIGHT))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(Cell().add(Paragraph()).setBorder(Border.NO_BORDER))
        table.addCell(
            Cell().add(innerTable.setHorizontalAlignment(HorizontalAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell(1, 2).add(
                Paragraph(nameReceiverCompany)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
            ).setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(
                Paragraph(getString(R.string.check_service_point)).setTextAlignment(
                    TextAlignment.LEFT
                )
            )
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(Paragraph("90530004214").setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(Paragraph(getString(R.string.check_terminal)).setTextAlignment(TextAlignment.LEFT))
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(Paragraph("92600042").setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(
                Paragraph(getString(R.string.check_receiver_card)).setTextAlignment(
                    TextAlignment.LEFT
                )
            )
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(Paragraph(checkData.receiverPan).setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(Paragraph(getString(R.string.check_receiver)).setTextAlignment(TextAlignment.LEFT))
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(Paragraph(checkData.receiverName).setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(
                Paragraph(getString(R.string.check_time_created)).setTextAlignment(
                    TextAlignment.LEFT
                )
            )
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(Paragraph(Text("${checkData.time}")).setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(
                Paragraph(getString(R.string.check_time_payment)).setTextAlignment(
                    TextAlignment.LEFT
                )
            )
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(Paragraph(Text("${checkData.time}")).setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(
                Paragraph(getString(R.string.check_sender_card)).setTextAlignment(
                    TextAlignment.LEFT
                )
            )
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(Paragraph(checkData.senderPan).setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(
                Paragraph(getString(R.string.check_service_cost)).setTextAlignment(
                    TextAlignment.LEFT
                )
            )
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(
                Paragraph(Text("${checkData.fee} sum")).setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
            ).setBorder(Border.NO_BORDER)
        )

        table.addCell(
            Cell().add(
                Paragraph(getString(R.string.check_payment_cost)).setTextAlignment(
                    TextAlignment.LEFT
                )
            )
                .setBorder(Border.NO_BORDER)
        )
        table.addCell(
            Cell().add(
                Paragraph(Text("${checkData.totalCost} sum")).setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
            ).setBorder(Border.NO_BORDER)
        )

        document.add(table)
        document.close()
//        Toast.makeText(requireContext(), "PDF created", Toast.LENGTH_LONG).show()
        shareAnyFile(file)
    }

    private fun getPDFImage(imageResId: Int, height: Float? = null, width: Float? = null): Image {
        val drawable = AppCompatResources.getDrawable(requireContext(), imageResId)
        val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        val imageData: ImageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        height?.let {
            image.setHeight(height)
        }
        width?.let {
            image.setWidth(width)
        }
        return image
    }

    private fun sharePdf(file: File) {
        val intentShareFile = Intent(Intent.ACTION_SEND)
        intentShareFile.type = "application/pdf"

        intentShareFile.putExtra(
            Intent.EXTRA_INTENT,
            file
        )

        intentShareFile.putExtra(
            Intent.EXTRA_SUBJECT,
            "Receipt"
        )

        /*intentShareFile.putExtra(
            Intent.EXTRA_TEXT,
            "Your Receipt"
        )*/

        requireContext().startActivity(
            Intent.createChooser(
                intentShareFile,
                "Check PDF"
            )
        )
    }

    private fun shareAnyFile(file: File) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        val screenshotUri: Uri = Uri.parse(file.path)
        sharingIntent.type = "*/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Check PDF"));
    }
}