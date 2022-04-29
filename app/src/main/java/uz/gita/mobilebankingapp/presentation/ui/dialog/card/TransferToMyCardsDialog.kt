package uz.gita.mobilebankingapp.presentation.ui.dialog.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.databinding.DialogTransferToMyCardsBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.TransferToMyCardsAdapter
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class TransferToMyCardsDialog(private val cardsList: List<CardData>) : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogTransferToMyCardsBinding::bind)
    private val adapter by lazy { TransferToMyCardsAdapter(cardsList) }

    private var listener: ((Int) -> Unit)? = null
    fun setListener(f: (Int) -> Unit) {
        listener = f
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
        return inflater.inflate(R.layout.dialog_transfer_to_my_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        rvMyReceivingCards.adapter = adapter
        rvMyReceivingCards.layoutManager = LinearLayoutManager(requireContext())

        if (cardsList.isEmpty()) {
            viewEmptyList.visible()
        } else {
            viewEmptyList.gone()
        }

        viewEmptyList.setOnClickListener {
            dismiss()
        }

        adapter.setListener { adapterPos ->
            listener?.invoke(adapterPos)
            dismiss()
        }
    }
}
