package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
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
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.CheckData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.CheckDialogButtonsEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenCheckTransferBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.main.CheckTransferDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showToast
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
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
        costService.text = "${getPortableAmount("${args.checkData.fee.toInt()}")} sum"
        senderPan.text = args.checkData.senderPan
        amountPayment.text = getPortableAmount("${args.checkData.totalCost.toInt()}")
        dateAndTime.text =
            SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US).format(Date(args.checkData.time))

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        bottomBar.imgRetry.setOnClickListener {
            showFancyToast(
                "Retry",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        bottomBar.imgSave.setOnClickListener {
            showFancyToast(
                "Save",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        bottomBar.imgCheck.setOnClickListener {
            val checkDialog = CheckTransferDialog(args.checkData)
            checkDialog.setShareBtnListener { buttonType ->
                when (buttonType) {
                    CheckDialogButtonsEnum.DOWNLOAD -> {
                        showFancyToast(
                            "Download",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.INFO
                        )
                    }
                    CheckDialogButtonsEnum.SHARE -> {
                        Permissions.check(requireContext(), arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), null, null,
                            object : PermissionHandler() {
                                override fun onGranted() {
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
                            }
                        )
                    }
                    CheckDialogButtonsEnum.PRINT_QR -> {
                        showFancyToast(
                            "Print QR",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.INFO
                        )
                    }
                }
            }
            checkDialog.isCancelable = true
            checkDialog.show(childFragmentManager, "check_dialog")
        }

        bottomBar.imgCancel.setOnClickListener {
            showFancyToast(
                "Cancel",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
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
                Paragraph(Text("${getPortableAmount("${checkData.fee}")} sum")).setBold()
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
                Paragraph(Text("${getPortableAmount("${checkData.totalCost}")} sum")).setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
            ).setBorder(Border.NO_BORDER)
        )

        document.add(table)
        document.close()
        share()
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

    private fun share() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.apply {
            type = "*/*"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Receipt Transaction"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Check file"
            )
        }
        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "receiptPdf.pdf"
        )
        /* val fileUri =
             getUriForFile(requireActivity(), "${requireContext().packageName}.provider", file)
         sharingIntent.putExtra(Intent.EXTRA_STREAM, fileUri)*/

        val fileUri: Uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().applicationContext.packageName}.provider",
            file
        )
        sharingIntent.putExtra(Intent.EXTRA_STREAM, fileUri)

        println("--------------------------------------------------------------")
        println(fileUri)
        println("--------------------------------------------------------------")

        val chooser = Intent.createChooser(sharingIntent, "Check PDF")
        val resInfoList: List<ResolveInfo> = requireContext().packageManager.queryIntentActivities(
            chooser,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            requireContext().grantUriPermission(
                packageName,
                fileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        startActivity(chooser)
    }   // Others didn't work properly.

    private fun share2() {
        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "receiptPdf.pdf"
        )
        if (file != null) {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName.toString() + ".provider",
                file
            )
            val i = Intent(Intent.ACTION_SEND)
            i.apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
//            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("fake@fake.edu"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Check Subject")
            i.putExtra(Intent.EXTRA_TEXT, "Check Message")
            i.putExtra(Intent.EXTRA_STREAM, uri)
            i.type = "*/*"

            val chooser = Intent.createChooser(i, "Check PDF")
            val resInfoList: List<ResolveInfo> =
                requireContext().packageManager.queryIntentActivities(
                    chooser,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            requireContext().startActivity(Intent.createChooser(i, "Share you on the jobing"))
        }
    }

    private fun share3() {
        val intentShareFile = Intent(Intent.ACTION_SEND)
        val fileWithinMyDir = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "receiptPdf.pdf"
        )
        println("--------------------------------------------------------------")
        println(fileWithinMyDir)
        println("--------------------------------------------------------------")

        if (!fileWithinMyDir.exists()) {
            fileWithinMyDir.mkdirs()
        }
        if (fileWithinMyDir.exists()) {
            intentShareFile.type = "*/*"
            intentShareFile.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("content://" + fileWithinMyDir.absolutePath)
            )
            intentShareFile.putExtra(
                Intent.EXTRA_SUBJECT,
                "Check"
            )
            requireContext().startActivity(Intent.createChooser(intentShareFile, "Share File"))
        } else {
            showToast("File does not exist");
        }
    }

    private fun share4() {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "receiptPdf.pdf"
        )

        if (!filePath.exists()) {
            filePath.createNewFile()
        }

        val fileOutputStream = FileOutputStream(filePath)
//        hssfWorkbook.write(fileOutputStream)

        fileOutputStream.flush()
        fileOutputStream.close()

        val emailIntent = Intent(Intent.ACTION_SEND)
        val uri = FileProvider.getUriForFile(requireContext(), "com.example.counter", filePath)
        emailIntent.type = "*/*"
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sending")
        emailIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(emailIntent)
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