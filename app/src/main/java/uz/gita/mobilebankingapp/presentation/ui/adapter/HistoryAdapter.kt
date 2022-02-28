package uz.gita.mobilebankingapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.data.enums.TransactionTypes
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse
import java.util.*

class HistoryAdapter :
    PagingDataAdapter<MoneyTransferResponse.HistoryData, HistoryAdapter.HistoryViewHolder>(
        MyDiffUtil
    ) {

    private var itemClickListener: ((MoneyTransferResponse.HistoryData) -> Unit)? = null
    fun setItemClickListener(block: (MoneyTransferResponse.HistoryData) -> Unit) {
        itemClickListener = block
    }

    private val transactionTypes = arrayOf(
        TransactionTypes.AIR,
        TransactionTypes.AUTO,
        TransactionTypes.ACCESSORIES,
        TransactionTypes.RENT,
        TransactionTypes.CONVERSION,
        TransactionTypes.CREDIT,
        TransactionTypes.SHOP,
        TransactionTypes.REST,
        TransactionTypes.TAXI,
        TransactionTypes.TRANSFER,
        TransactionTypes.TRANSPORT
    )

    object MyDiffUtil : DiffUtil.ItemCallback<MoneyTransferResponse.HistoryData>() {
        override fun areItemsTheSame(
            oldItem: MoneyTransferResponse.HistoryData,
            newItem: MoneyTransferResponse.HistoryData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MoneyTransferResponse.HistoryData,
            newItem: MoneyTransferResponse.HistoryData
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textMoney = view.findViewById<TextView>(R.id.textMoney)
        private val transactionType =
            view.findViewWithTag<TextView>(R.id.txt_transaction_type_history)
//        private val textFee = view.findViewById<TextView>(R.id.textFee)

        init {
            itemView.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition)!!)
            }
        }

        //status 0 -> pul tushdi, else -> pul yechib olindi
        fun bind() {
            getItem(absoluteAdapterPosition)?.let { data ->
                if (data.status == 0) {
                    textMoney.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorGreen
                        )
                    )
                    textMoney.text = "+${data.amount}"
                } else {
                    textMoney.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorRed
                        )
                    )
                    textMoney.text = "-${data.amount}"
                }
//                    textFee.text = it.fee.toString()

               /* DrawableCompat.setTint(
                    transactionType.background,
                    ContextCompat.getColor(
                        App.instance,
                        transactionTypes[Random().nextInt(4)].getBgTintColor()
                    )
                )*/
            }
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
}
