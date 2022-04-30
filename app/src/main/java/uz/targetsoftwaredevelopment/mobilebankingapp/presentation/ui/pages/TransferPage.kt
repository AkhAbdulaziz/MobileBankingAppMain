package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.pages

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.RecipientData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.PageTransferBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.transferPageAdapters.RecipientsAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.card.TransferToMyCardsDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.pages.TransferViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.pages.TransferViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showToast
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible

@AndroidEntryPoint
class TransferPage : Fragment(R.layout.page_transfer) {
    private val binding by viewBinding(PageTransferBinding::bind)
    private val viewModel: TransferViewModel by viewModels<TransferViewModelImpl>()
    private var recipientsList = ArrayList<RecipientData>()
    private val recipientsAdapter by lazy { RecipientsAdapter(recipientsList) }
    private var isFirstTime: Boolean = true

    private var isReadyCardNumber = false
    private var isReadyMoney = false
    private var receiverName: String = ""
    /*private var directionType: String = "${PaymentPageEnum.NONE.name}"
    private val args: TransferPageArgs by navArgs()*/

    // TODO: back buttonni bosganda background bilinib qolyapti

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressedListener?.invoke(1)
                }
            })
        /*    directionType = if (args.directionType == PaymentPageEnum.NONE) {
                arguments?.getString("direction_type")!!
            } else {
                args.directionType.name
            }*/

        /*if (directionType != null && directionType == PaymentPageEnum.FROM_BASE_SCREEN.name) {
            backBtn.invisible()
        } else {
            backBtn.visible()
        }*/

        if (isFirstTime) {
            fillRecipientsList()
            isFirstTime = false
        }

        rvRecipients.adapter = recipientsAdapter
        rvRecipients.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recipientsAdapter.setExistRecipientClickListener {
            showToast("Recipient $it")
        }
        recipientsAdapter.setNewRecipientClickListener {
            showToast("Add new recipient")
        }

        /*  backBtn.setOnClickListener {
              findNavController().popBackStack()
          }*/

        btnTransferToMyCards.setOnClickListener {
            viewModel.getAllCardList()
        }

        cardNumberEditText.addTextChangedListener {
            it?.let {
                isReadyCardNumber = it.toString().length == 16 && it.contains("8600")
                check()
            }
            if (isReadyCardNumber) {
//                 Shu joyida crash beryatgandi

                /* viewModel.getOwnerByPan(
                     OwnerByPanRequest(
                         it.toString()
                     )
                 )*/
            }
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
        viewModel.cardsListLiveData.observe(viewLifecycleOwner, cardsListObserver)
        viewModel.panToTransferPageLiveData.observe(
            viewLifecycleOwner,
            panToTransferPageObserver
        )
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
        showToast(it)
    }

    private val successObserver = Observer<String> {
        showToast(it)
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

    private fun fillRecipientsList() {
        recipientsList.apply {
            add(
                RecipientData(
                    "SP",
                    "Steven Paul Jobs",
                    R.color.colorRed
                )
            )
            add(
                RecipientData(
                    "WB",
                    "William Bradley Pitt",
                    R.color.yellow
                )
            )
            add(
                RecipientData(
                    "WH",
                    "William Henry Gates",
                    R.color.colorGreen
                )
            )
            add(
                RecipientData(
                    "ME",
                    "Mark Elliot Zuckerberg",
                    R.color.orange
                )
            )
            add(
                RecipientData(
                    "SM",
                    "Sergey Mikhaylovich Brin",
                    R.color.purple_500
                )
            )
            add(
                RecipientData(
                    "LE",
                    "Lawrence Edward Page",
                    R.color.brown
                )
            )
            add(
                RecipientData(
                    "PS",
                    "Pichai Sundararajan",
                    R.color.orange
                )
            )
            add(
                RecipientData(
                    "",
                    "Add Recipient",
                    R.color.orange,
                    true
                )
            )
        }
    }

    private fun check() {
        binding.nextBtn.isEnabled =
            isReadyCardNumber && isReadyMoney
    }
}