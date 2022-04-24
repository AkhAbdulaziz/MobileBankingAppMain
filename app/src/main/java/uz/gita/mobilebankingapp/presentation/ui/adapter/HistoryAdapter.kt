package uz.gita.mobilebankingapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter :
    PagingDataAdapter<MoneyTransferResponse.HistoryData, HistoryAdapter.HistoryViewHolder>(
        MyDiffUtil
    ) {

    private val transactionTypes = arrayListOf(
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

    private var itemClickListener: ((MoneyTransferResponse.HistoryData) -> Unit)? = null
    fun setItemClickListener(block: (MoneyTransferResponse.HistoryData) -> Unit) {
        itemClickListener = block
    }

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
        private val imageLogo = view.findViewById<ImageView>(R.id.imgLogo)
        private val textMoney = view.findViewById<TextView>(R.id.txtMoney)
        private val textTime = view.findViewById<TextView>(R.id.txtTime)
        private val transactionType = view.findViewById<TextView>(R.id.txt_transaction_type_history)

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
                imageLogo.setImageResource(R.drawable.trastbank_logo)

                /* textTime.text = String.format(
                     "%02d day, %02d hour, %02d min, %02d sec",
                     TimeUnit.MILLISECONDS.toDays(data.time),
                     TimeUnit.MILLISECONDS.toHours(data.time),
                     TimeUnit.MILLISECONDS.toMinutes(data.time),
                     TimeUnit.MILLISECONDS.toSeconds(data.time) -
                             TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(data.time))
                 )*/
                textTime.text =
                    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US).format(Date(data.time))

                var randomTransactionType = (transactionTypes[(0..10).random()])
                transactionType.text = randomTransactionType.name.lowercase()
                DrawableCompat.setTint(
                    transactionType.background,
                    ContextCompat.getColor(App.instance, randomTransactionType.getBgTintColor())
                )
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
