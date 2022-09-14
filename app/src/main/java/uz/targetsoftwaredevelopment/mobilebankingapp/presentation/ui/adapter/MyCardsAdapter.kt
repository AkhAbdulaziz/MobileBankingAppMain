package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.cardBgImagesList
import java.util.*

class MyCardsAdapter(private val list: List<CardData>) :
    RecyclerView.Adapter<MyCardsAdapter.VH>() {

    private var listener: ((Int) -> Unit)? = null
    fun setListener(f: (Int) -> Unit) {
        listener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val cardName = view.findViewById<TextView>(R.id.cardName)
        private val cardBalance = view.findViewById<TextView>(R.id.cardBalanceText)
        private val cardOwner = view.findViewById<TextView>(R.id.cardOwnerFullname)
        private val cardNumber = view.findViewById<TextView>(R.id.cardNumberText)
        private val cardValidate = view.findViewById<TextView>(R.id.cardValidateText)
        private val cardBg = view.findViewById<ImageView>(R.id.cardBg)

        init {
            itemView.setOnClickListener {
                listener?.invoke(absoluteAdapterPosition)
                return@setOnClickListener
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind() {
            val asterisk = Char(42)
            val data = list[absoluteAdapterPosition]
            cardName.text = data.cardName
            cardBalance.text = "${getPortableAmount("${data.balance}")} sum"
            cardOwner.text = data.owner
            cardNumber.text =
                "${data.pan!!.substring(0, 4)} ${
                    data.pan.substring(
                        4,
                        6
                    )
                }$asterisk$asterisk $asterisk$asterisk$asterisk$asterisk ${data.pan.substring(12)}"
            cardValidate.text = data.exp
            if (data.color != null) {
                cardBg.setImageResource(cardBgImagesList[data.color])
            } else {
                cardBg.setImageResource(cardBgImagesList[Random().nextInt(16)])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size

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