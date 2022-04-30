package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.card

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request.DeleteCardRequest
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenMyCardsBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.MyCardsAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.card.ClarifyDeleteCardDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.card.EventDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.card.QRToReceiveTransferDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.card.MyCardsViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.card.MyCardsViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.gone
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showToast
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible

@AndroidEntryPoint
class MyCardsScreen : Fragment(R.layout.screen_my_cards) {
    private val binding by viewBinding(ScreenMyCardsBinding::bind)
    private val viewModel: MyCardsViewModel by viewModels<MyCardsViewModelImpl>()
    private lateinit var eventDialog: EventDialog
    private lateinit var qrToReceiveTransferDialog: QRToReceiveTransferDialog
    private var cardsList = ArrayList<CardData>()
    private val adapter by lazy { MyCardsAdapter(cardsList) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        refresh.isRefreshing = true
        viewModel.getAllCardList()

        refresh.setOnRefreshListener {
            viewModel.getAllCardList()
            refresh.isRefreshing = true
        }

        viewModel.cardsListLiveData.observe(viewLifecycleOwner, cardsListObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.closeDialogLiveData.observe(viewLifecycleOwner, closeDialogObserver)
        viewModel.openLoginScreenLiveData.observe(viewLifecycleOwner, openLoginScreenObserver)

        cardsRecyclerView.adapter = adapter
        cardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        addCardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myCardsScreen_to_addCardScreen)
        }

        adapter.setListener { adapterPos ->
            eventDialog = EventDialog(cardsList[adapterPos])
            eventDialog.apply {
                setDeleteListener {
                    dismiss()
                    val clarifyDeleteDialog = ClarifyDeleteCardDialog()
                    clarifyDeleteDialog.setPositiveBtnListener {
                        viewModel.deleteCard(
                            DeleteCardRequest(
                                cardsList[adapterPos].pan!!
                            )
                        )
                        cardsList.removeAt(adapterPos)
                        adapter.notifyItemRemoved(adapterPos)
                    }
                    clarifyDeleteDialog.show(childFragmentManager, "event")
                }
                setSettingsListener {
                    dismiss()
                    findNavController().navigate(
                        MyCardsScreenDirections.actionMyCardsScreenToSettingsCardScreen(
                            cardsList[adapterPos]
                        )
                    )
                }
                setShareQRListener {
                    qrToReceiveTransferDialog = QRToReceiveTransferDialog(cardsList[adapterPos])
                    qrToReceiveTransferDialog.apply {
                        setSaveQRButtonClickListener {
                            showToast("Save QR")
                            dismiss()
                        }
                        setShareQRButtonClickListener {
                            showToast("Share QR")
                            dismiss()
                        }
                        setPrintQRButtonClickListener {
                            showToast("Print QR")
                            dismiss()
                        }
                    }
                    qrToReceiveTransferDialog.show(childFragmentManager, "qr_receive_transfer")
                }
            }
            eventDialog.show(childFragmentManager, "event")
        }
    }

    private val openLoginScreenObserver = Observer<Unit> {
        findNavController().navigate(
            MyCardsScreenDirections.actionMyCardsScreenToPinCodeScreen(
                StartScreenEnum.MAIN
            )
        )
    }

    private val cardsListObserver = Observer<List<CardData>> {
        binding.scope {
            if (it.isEmpty()) {
                cardsRecyclerView.gone()
                imgEmpty.visible()
                txtEmpty.visible()
            } else {
                imgEmpty.gone()
                txtEmpty.gone()
                cardsRecyclerView.visible()
            }
            cardsList.clear()
            cardsList.addAll(it)
            refresh.isRefreshing = false
            adapter.notifyDataSetChanged()
        }
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

    private val closeDialogObserver = Observer<Unit> {
        eventDialog.dismiss()
    }
}
