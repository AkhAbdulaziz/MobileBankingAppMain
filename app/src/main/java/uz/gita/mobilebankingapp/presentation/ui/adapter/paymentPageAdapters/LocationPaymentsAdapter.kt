package uz.gita.mobilebankingapp.presentation.ui.adapter.paymentPageAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.LocationPaymentsData
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.visible

class LocationPaymentsAdapter(private val list: List<LocationPaymentsData>) :
    RecyclerView.Adapter<LocationPaymentsAdapter.VH>() {

    private var locationOnLickListener: ((Int) -> Unit)? = null
    fun setLocationOnClickListener(f: (Int) -> Unit) {
        locationOnLickListener = f
    }

    private var errorListener: ((Int) -> Unit)? = null
    fun setErrorListener(f: (Int) -> Unit) {
        errorListener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {

        private val imgAlertError = view.findViewById<ImageView>(R.id.img_error_alert)
        private val descriptionError = view.findViewById<TextView>(R.id.txt_error_description)
        private val errorEnableText = view.findViewById<TextView>(R.id.txt_error_enable)
        private val imgLocation = view.findViewById<ImageView>(R.id.img_location)
        private val nameLocation = view.findViewById<TextView>(R.id.txt_name_location)
        private val addressLocation = view.findViewById<TextView>(R.id.txt_address_location)
        private val distanceLocation = view.findViewById<TextView>(R.id.txt_distance_location)
        private val tag = view.findViewById<TextView>(R.id.txt_tag_location)
        private val underlineLocation = view.findViewById<View>(R.id.underline_location)


        init {
            itemView.setOnClickListener {
                if (list[absoluteAdapterPosition].imgAlertError != null) {
                    errorListener?.invoke(absoluteAdapterPosition)
                    return@setOnClickListener
                } else {
                    locationOnLickListener?.invoke(absoluteAdapterPosition)
                    return@setOnClickListener
                }
            }
        }

        fun bind() {
            val data = list[absoluteAdapterPosition]
            if (data.imgAlertError != null) {
                itemView.setBackgroundResource(R.drawable.bg_location_payments_error)
                imgAlertError.setImageResource(data.imgAlertError!!)
                imgAlertError.visible()
                descriptionError.text = data.descriptionError
                descriptionError.visible()
                errorEnableText.visible()

                imgLocation.gone()
                nameLocation.gone()
                addressLocation.gone()
                distanceLocation.gone()
                tag.gone()
                underlineLocation.gone()
            } else {
                if (data.isUnderlineVisible) {
                    underlineLocation.visible()
                }else{
                    underlineLocation.gone()
                }
                imgAlertError.gone()
                descriptionError.gone()
                errorEnableText.gone()

                imgLocation.setImageResource(data.imgLocation!!)
                imgLocation.visible()
                nameLocation.text = data.nameLocation
                nameLocation.visible()
                addressLocation.text = data.addressLocation
                addressLocation.visible()
                distanceLocation.text = data.distanceLocation
                distanceLocation.visible()
                if (data.tag != null) {
                    tag.text = data.tag
                    tag.visible()
                } else {
                    tag.gone()
                }
            }

            /* DrawableCompat.setTint(
                 transactionType.background,
                 ContextCompat.getColor(App.instance, data.transactionType.getBgTintColor())
             )*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_location_payments, parent, false)
        )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}