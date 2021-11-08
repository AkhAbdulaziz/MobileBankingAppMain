package uz.gita.mobilebankingapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.response.MoneyTransferResponse

class HistoryAdapter :
    PagingDataAdapter<MoneyTransferResponse.HistoryData, HistoryAdapter.HistoryViewHolder>(
        MyDiffUtil
    ) {

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
        private val textFee = view.findViewById<TextView>(R.id.textFee)

        fun bind() {
            getItem(absoluteAdapterPosition)?.let {
                if (it.status == 0) {
                    itemView.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.purple_500
                        )
                    )
                } else {
                    itemView.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.teal_700
                        )
                    )
                }
                textMoney.text = it.amount.toString()
                textFee.text = it.fee.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        )
}
