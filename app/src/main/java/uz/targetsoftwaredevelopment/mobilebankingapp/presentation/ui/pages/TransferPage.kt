package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.pages

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.ImageCapture
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.PageTransferBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.RecipientsAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.card.TransferToMyCardsDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.pages.TransferViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.pages.TransferViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.recipientsList
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible
import java.io.File
import java.util.concurrent.ExecutorService

@AndroidEntryPoint
class TransferPage : Fragment(R.layout.page_transfer) {
    private val binding by viewBinding(PageTransferBinding::bind)
    private val viewModel: TransferViewModel by viewModels<TransferViewModelImpl>()
    private val recipientsAdapter by lazy { RecipientsAdapter(recipientsList) }

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var isReadyCardNumber = false
    private var isReadyMoney = false
    private var receiverName: String = ""

    private var backPressedListener: ((Int) -> Unit)? = null
    fun setBackPressedListener(f: (Int) -> Unit) {
        backPressedListener = f
    }

    private var openSendMoneyScreenListener: ((String, Long, String) -> Unit)? = null
    fun setOpenSendMoneyScreenListener(block: (String, Long, String) -> Unit) {
        openSendMoneyScreenListener = block
    }

    private var openPinCodeScreenListener: ((StartScreenEnum) -> Unit)? = null
    fun setOpenPinCodeScreenListener(block: (StartScreenEnum) -> Unit) {
        openPinCodeScreenListener = block
    }

    private var openContactsScreenListener: (() -> Unit)? = null
    fun setOpenContactsScreenListener(f: () -> Unit) {
        openContactsScreenListener = f
    }

    private var openScanCardScreenListener: (() -> Unit)? = null
    fun setOpenScanCardScreenListener(f: () -> Unit) {
        openScanCardScreenListener = f
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressedListener?.invoke(1)
                }
            })

        rvRecipients.adapter = recipientsAdapter
        rvRecipients.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recipientsAdapter.setExistRecipientClickListener {
            showFancyToast(
                "Recipient $it",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }
        recipientsAdapter.setNewRecipientClickListener {
            showFancyToast(
                "Add new recipient",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        btnTransferToMyCards.setOnClickListener {
            viewModel.getAllCardList()
        }

        cardNumberEditText.addTextChangedListener {
            it?.let {
                isReadyCardNumber = it.toString().length == 16 && it.contains("8600")
                check()
            }
            /* Shu joyida crash beryatgandi
            if (isReadyCardNumber) {
                 viewModel.getOwnerByPan(
                     OwnerByPanRequest(
                         it.toString()
                     )
                 )
            }
            */
        }

        moneyAmountEditText.addTextChangedListener {
            it?.let {
                isReadyMoney = if (it.toString().isEmpty()) {
                    false
                } else {
                    (it.toString()).toLong() >= 500
                }
                check()
            }
        }

        imgScanCardNumber.setOnClickListener {
            openScanCardScreenListener?.invoke()
        }

        imgContact.setOnClickListener {
            openContactsScreenListener?.invoke()
        }

        nextBtn.setOnClickListener {
            openSendMoneyScreenListener?.invoke(
                cardNumberEditText.text.toString(),
                moneyAmountEditText.text.toString().toLong(),
                receiverName
            )
        }

        viewModel.enableNextButton.observe(viewLifecycleOwner, enableNextObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.successLiveData.observe(viewLifecycleOwner, successObserver)
        viewModel.ownerNameLiveData.observe(viewLifecycleOwner, ownerNameObserver)
        viewModel.openLoginScreenLiveData.observe(viewLifecycleOwner, openLoginScreenObserver)
        viewModel.cardsListLiveData.observe(this@TransferPage, cardsListObserver)
        viewModel.panToTransferPageLiveData.observe(
            viewLifecycleOwner,
            panToTransferPageObserver
        )
        viewModel.scannedCardNumberLiveData.observe(this@TransferPage, scannedCardNumberObserver)
    }

    private val scannedCardNumberObserver = Observer<String> {
        binding.cardNumberEditText.setText(it)
    }

    private val openLoginScreenObserver = Observer<Unit> {
        openPinCodeScreenListener?.invoke(
            StartScreenEnum.MAIN
        )
    }

    private val enableNextObserver = Observer<Unit> {
        binding.nextBtn.isEnabled = true
    }

    private val errorObserver = Observer<String> {
        showFancyToast(
            it,
            FancyToast.LENGTH_SHORT,
            FancyToast.ERROR
        )
    }

    private val successObserver = Observer<String> {
        showFancyToast(
            it,
            FancyToast.LENGTH_SHORT,
            FancyToast.INFO
        )
    }

    private val ownerNameObserver = Observer<String> { ownerName ->
        receiverName = ownerName
        binding.cardOwnerNameText.apply {
            text = "Kartaning egasi: $ownerName"
            visible()
        }
    }

    private val cardsListObserver = Observer<List<CardData>> { cardsList ->
        val dialog = TransferToMyCardsDialog(cardsList)
        dialog.setListener { adapterPos ->
            val cardData = cardsList[adapterPos]
            binding.scope {
                cardNumberEditText.setText(cardData.pan)
                moneyAmountEditText.apply {
                    requestFocus()

                    // Show keyboard
                    val inputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(this, 0)
                }
            }
        }
        dialog.isCancelable = true
        dialog.show(childFragmentManager, "transfer_to_my_cards_dialog")
    }

    private val panToTransferPageObserver = Observer<String> { pan ->
        binding.cardNumberEditText.setText(pan)
    }

    private fun check() {
        binding.nextBtn.isEnabled =
            isReadyCardNumber && isReadyMoney
    }
}