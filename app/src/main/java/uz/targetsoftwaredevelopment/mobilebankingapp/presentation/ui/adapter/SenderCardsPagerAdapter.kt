package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.items.SenderCardItem
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.putArguments
class SenderCardsPagerAdapter(
    private val receiverCardPan: String,
    private val moneyAmount: Long,
    private val cardsList: List<CardData>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private var disableSendButtonListener: (() -> Unit)? = null
    fun setDisableSendButtonListener(f: () -> Unit) {
        disableSendButtonListener = f
    }

    private var enableSendButtonListener: (() -> Unit)? = null
    fun setEnableSendButtonListener(f: () -> Unit) {
        enableSendButtonListener = f
    }

    override fun getItemCount(): Int = cardsList.size

    override fun createFragment(position: Int): Fragment {
        return SenderCardItem().apply {
            putArguments {
                putSerializable("SENDER_CARD", cardsList[position])
                putLong("MONEY_AMOUNT", moneyAmount)
                putString("RECEIVER_CARD_PAN", receiverCardPan)
            }
            setDisableSendButtonListener {
                disableSendButtonListener?.invoke()
            }
            setEnableSendButtonListener {
                enableSendButtonListener?.invoke()
            }
        }
    }
}

