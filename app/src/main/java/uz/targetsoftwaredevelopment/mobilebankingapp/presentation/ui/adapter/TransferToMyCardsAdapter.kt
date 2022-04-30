package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

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

class TransferToMyCardsAdapter(private val list: List<CardData>) :
    RecyclerView.Adapter<TransferToMyCardsAdapter.VH>() {

    private var listener: ((Int) -> Unit)? = null
    fun setListener(f: (Int) -> Unit) {
        listener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val cardName = view.findViewById<TextView>(R.id.txtCardName)
        private val cardBalance = view.findViewById<TextView>(R.id.txtCardBalance)
        private val cardNumber = view.findViewById<TextView>(R.id.txtCardNumber)
        private val cardBg = view.findViewById<ImageView>(R.id.imgCardBg)

        init {
            itemView.setOnClickListener {
                listener?.invoke(absoluteAdapterPosition)
                return@setOnClickListener
            }
        }

        fun bind() {
            val data = list[absoluteAdapterPosition]
            cardName.text = data.cardName
            cardBalance.text = "${data.balance} sum"
            cardNumber.text = "* ${data.pan!!.substring(12)}"
            if (data.color != null) {
                cardBg.setImageResource(cardBgImagesList[data.color])
            } else {
                cardBg.setImageResource(cardBgImagesList[Random().nextInt(16)])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_my_receiving_cards, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}