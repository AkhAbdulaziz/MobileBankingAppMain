package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.PaymentForServicesData
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.setMarginsInDp

class PaymentForServicesAdapter(private val list: List<PaymentForServicesData>) :
    RecyclerView.Adapter<PaymentForServicesAdapter.VH>() {

    private var serviceOnLickListener: ((String) -> Unit)? = null
    fun setServiceOnClickListener(f: (String) -> Unit) {
        serviceOnLickListener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.img_logo_service)
        private val description = view.findViewById<TextView>(R.id.txt_description_service)

        init {
            itemView.setOnClickListener {
                serviceOnLickListener?.invoke(list[absoluteAdapterPosition].description)
                return@setOnClickListener
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
            image.setImageResource(data.image)
            description.text = data.description
            description.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                if (data.description.length >= 30) {
                    10f
                } else if (data.description.length in 21..29) {
                    12f
                } else {
                    14f
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payment_for_services, parent, false)
        )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}