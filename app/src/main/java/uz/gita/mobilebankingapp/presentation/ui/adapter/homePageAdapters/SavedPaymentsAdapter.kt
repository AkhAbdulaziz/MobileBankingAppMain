package uz.gita.mobilebankingapp.presentation.ui.adapter.homePageAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.data.entities.SavedPaymentData
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.setMarginsInDp
import uz.gita.mobilebankingapp.utils.visible

class SavedPaymentsAdapter(private val list: List<SavedPaymentData>) :
    RecyclerView.Adapter<SavedPaymentsAdapter.VH>() {

    private var savedPaymentOnClickListener: ((Int) -> Unit)? = null
    fun setSavedPaymentOnClickListener(f: (Int) -> Unit) {
        savedPaymentOnClickListener = f
    }

    private var newButtonOnClickListener: (() -> Unit)? = null
    fun setNewButtonOnClickListener(f: () -> Unit) {
        newButtonOnClickListener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val logo = view.findViewById<ImageView>(R.id.img_logo)
        private val title = view.findViewById<TextView>(R.id.txt_title)
        private val number = view.findViewById<TextView>(R.id.txt_number)
        private val addImage = view.findViewById<ImageView>(R.id.btn_add_saved_payment)

        init {
            itemView.setOnClickListener {
                if (list[absoluteAdapterPosition].isLast) {
                    newButtonOnClickListener?.invoke()
                    return@setOnClickListener
                } else {
                    savedPaymentOnClickListener?.invoke(absoluteAdapterPosition)
                    return@setOnClickListener
                }
            }
        }

        fun bind() {
            val data = list[absoluteAdapterPosition]
            when (absoluteAdapterPosition) {
                0 -> {
                    itemView.setMarginsInDp(16f, 0f, 4f, 0f)
                }
                list.size - 1 -> {
                    itemView.setMarginsInDp(4f, 0f, 16f, 0f)
                }
                else -> {
                    itemView.setMarginsInDp(4f, 0f, 4f, 0f)
                }
            }
            if (data.isLast) {
                logo.gone()
                title.gone()
                number.gone()
                addImage.visible()
                (itemView as CardView).cardElevation = 0f
                (itemView as CardView).setCardBackgroundColor(
                    ContextCompat.getColor(
                        App.instance,
                        R.color.lightGreyColor
                    )
                )
            } else {
                logo.visible()
                title.visible()
                number.visible()
                addImage.gone()
                (itemView as CardView).cardElevation = 2.66f

                logo.setImageResource(data.logo)
                title.text = data.title
                number.text = data.number
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_saved_payments, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}