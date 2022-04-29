package uz.gita.mobilebankingapp.presentation.ui.adapter.items

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.databinding.ItemSenderCardBinding
import uz.gita.mobilebankingapp.utils.cardBgImagesList
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.visible
import java.util.*

@AndroidEntryPoint
class SenderCardItem : Fragment(R.layout.item_sender_card) {
    private val binding by viewBinding(ItemSenderCardBinding::bind)

    private var disableSendButtonListener: (() -> Unit)? = null
    fun setDisableSendButtonListener(f: () -> Unit) {
        disableSendButtonListener = f
    }

    private var enableSendButtonListener: (() -> Unit)? = null
    fun setEnableSendButtonListener(f: () -> Unit) {
        enableSendButtonListener = f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val cardData = it.getSerializable("SENDER_CARD") as CardData
            val moneyAmount = it.getLong("MONEY_AMOUNT", 0L)
            val receiverCardPan = it.getString("RECEIVER_CARD_PAN", "8600")

            if (cardData.color != null) {
                cardBg.setImageResource(cardBgImagesList[cardData.color])
            } else {
                cardBg.setImageResource(cardBgImagesList[Random().nextInt(16)])
            }

            senderName.text = cardData.cardName
            senderBalance.text = "Balance: ${cardData.balance} sum"
            senderCardNumber.text = "**** ${cardData.pan!!.substring(12)}"

            when {
                (cardData.pan == receiverCardPan) -> {
                    errorImg.visible()
                    errorText.text = getString(R.string.en_error_payment_not_suitable_card)
                    errorText.visible()
                    disableSendButtonListener?.invoke()
                }
                (cardData.balance!! < moneyAmount) -> {
                    errorImg.visible()
                    errorText.text = getString(R.string.en_error_payment_not_enough_money)
                    errorText.visible()
                    disableSendButtonListener?.invoke()
                }
                else -> {
                    errorImg.gone()
                    errorText.gone()
                    enableSendButtonListener?.invoke()
                }
            }
        }
    }
}