package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.app.App
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.RecentTransactionData
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.gone
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible

class RecentTransactionsAdapter(private val list: List<RecentTransactionData>) :
    RecyclerView.Adapter<RecentTransactionsAdapter.VH>() {

    private var transactionOnLickListener: ((Int) -> Unit)? = null
    fun setTransactionOnClickListener(f: (Int) -> Unit) {
        transactionOnLickListener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val bankLogo = view.findViewById<ImageView>(R.id.img_bank_logo)
        private val bankName = view.findViewById<TextView>(R.id.txt_bank_name)
        private val transactionDate = view.findViewById<TextView>(R.id.txt_transaction_date)
        private val transactionTime = view.findViewById<TextView>(R.id.txt_transaction_time)
        private val transactionAmount = view.findViewById<TextView>(R.id.txt_transaction_amount)
        private val transactionType = view.findViewById<TextView>(R.id.txt_transaction_type)

        init {
            itemView.setOnClickListener {
                transactionOnLickListener?.invoke(absoluteAdapterPosition)
                return@setOnClickListener
            }
        }

        fun bind() {
            val data = list[absoluteAdapterPosition]
            if (data.bankLogo != null) {
                bankLogo.setImageResource(data.bankLogo!!)
                bankLogo.visible()
                bankName.gone()
            } else {
                bankName.text = data.bankName.uppercase()
                bankName.visible()
                bankLogo.gone()
            }
            transactionDate.text = data.transactionDate
            transactionAmount.text = "${data.transactionAmount} sum"
            transactionTime.text = data.transactionTime
            transactionType.text = data.transactionType.name.lowercase()
            DrawableCompat.setTint(
                transactionType.background,
                ContextCompat.getColor(App.instance, data.transactionType.getBgTintColor())
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_transactions, parent, false)
        )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}