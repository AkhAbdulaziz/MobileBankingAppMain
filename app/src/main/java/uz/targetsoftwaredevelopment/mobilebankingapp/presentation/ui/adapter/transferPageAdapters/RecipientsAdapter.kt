package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.transferPageAdapters

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
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.RecipientData
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.gone
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.invisible
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.setMarginsInDp
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible

class RecipientsAdapter(private val list: List<RecipientData>) :
    RecyclerView.Adapter<RecipientsAdapter.VH>() {

    private var existRecipientClickListener: ((Int) -> Unit)? = null
    fun setExistRecipientClickListener(f: (Int) -> Unit) {
        existRecipientClickListener = f
    }

    private var newRecipientClickListener: (() -> Unit)? = null
    fun setNewRecipientClickListener(f: () -> Unit) {
        newRecipientClickListener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val abbreviationName = view.findViewById<TextView>(R.id.txtAbbreviationName)
        private val fullName = view.findViewById<TextView>(R.id.txtFullName)
        private val imgAddRecipient = view.findViewById<ImageView>(R.id.btn_add_recipient)

        init {
            itemView.setOnClickListener {
                if (list[absoluteAdapterPosition].isItLast) {
                    newRecipientClickListener?.invoke()
                    return@setOnClickListener
                } else {
                    existRecipientClickListener?.invoke(absoluteAdapterPosition)
                    return@setOnClickListener
                }
            }
        }

        fun bind() {
            val data = list[absoluteAdapterPosition]
            when (absoluteAdapterPosition) {
                0 -> {
                    itemView.setMarginsInDp(16f, 0f, 2f, 0f)
                }
                list.size - 1 -> {
                    itemView.setMarginsInDp(2f, 0f, 16f, 0f)
                }
                else -> {
                    itemView.setMarginsInDp(2f, 0f, 2f, 0f)
                }
            }

            if (data.isItLast) {
                abbreviationName.invisible()
                fullName.text = "Add\nRecipient"
                imgAddRecipient.visible()
            } else {
                abbreviationName.text = data.abbreviationName
                abbreviationName.visible()
                fullName.text = data.fullName
                imgAddRecipient.gone()
                DrawableCompat.setTint(
                    abbreviationName.background,
                    ContextCompat.getColor(App.instance, data.bgColor)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_recipients, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}