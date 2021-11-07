package uz.gita.mobilebankingapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.CardData

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

        fun bind() {
            val data = list[absoluteAdapterPosition]
            cardName.text = data.cardName
            cardBalance.text = "${data.balance} so'm"
            cardOwner.text = data.owner
            cardNumber.text = data.pan
            cardValidate.text = data.exp
            cardBg.setImageResource(R.drawable.card_bg1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size


}