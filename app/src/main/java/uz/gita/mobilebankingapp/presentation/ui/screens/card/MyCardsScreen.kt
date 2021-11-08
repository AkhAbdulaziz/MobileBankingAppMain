package uz.gita.mobilebankingapp.presentation.ui.screens.card

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.DeleteCardRequest
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.GetCardsData
import uz.gita.mobilebankingapp.databinding.ScreenMyCardsBinding
import uz.gita.mobilebankingapp.presentation.dialog.card.ClarifyDeleteCardDialog
import uz.gita.mobilebankingapp.presentation.dialog.card.EventDialog
import uz.gita.mobilebankingapp.presentation.ui.adapter.MyCardsAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.MyCardsViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.MyCardsViewModelImpl
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class MyCardsScreen : Fragment(R.layout.screen_my_cards) {
    private val binding by viewBinding(ScreenMyCardsBinding::bind)
    private val viewModel: MyCardsViewModel by viewModels<MyCardsViewModelImpl>()
    private lateinit var eventDialog: EventDialog
    private var cardsList = ArrayList<CardData>()
    private val adapter by lazy { MyCardsAdapter(cardsList) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        progress.visible()

        viewModel.cardsListLiveData.observe(viewLifecycleOwner, cardsListObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.closeDialogLiveData.observe(
            viewLifecycleOwner,
            closeDialogObserver
        )
        viewModel.getAllCardList()

        cardsRecyclerView.adapter = adapter
        cardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        addCardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myCardsScreen_to_addCardScreen)
        }

        adapter.setListener { adapterPos ->
            eventDialog = EventDialog()
            eventDialog.setDeleteListener {
                eventDialog.dismiss()
                val clarifyDeleteDialog = ClarifyDeleteCardDialog()
                clarifyDeleteDialog.setPositiveBtnListener {
                    viewModel.deleteCard(
                        DeleteCardRequest(
                            cardsList[adapterPos].pan!!
                        )
                    )
                    adapter.notifyItemRemoved(adapterPos)
                }
                clarifyDeleteDialog.show(childFragmentManager, "event")
            }
            eventDialog.setSettingsListener {
                eventDialog.dismiss()
                findNavController().navigate(
                    MyCardsScreenDirections.actionMyCardsScreenToSettingsCardScreen(
                        cardsList[adapterPos]
                    )
                )
            }
            eventDialog.show(childFragmentManager, "event")
        }
    }

    private val cardsListObserver = Observer<GetCardsData> {
        cardsList.clear()
        cardsList.addAll(it.data!!)
        binding.progress.gone()
        adapter.notifyDataSetChanged()
    }

    private val errorMessageObserver = Observer<String> {
        showToast(it)
    }

    private val closeDialogObserver = Observer<Unit> {
        eventDialog.dismiss()
    }
}
