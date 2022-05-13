package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.ServicesData

class ServicesAdapter(private val list: List<ServicesData>) :
    RecyclerView.Adapter<ServicesAdapter.VH>() {

    private var listener: ((String) -> Unit)? = null
    fun setListener(block: (String) -> Unit) {
        listener = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val imageService = view.findViewById<ImageView>(R.id.imgService)
        private val title = view.findViewById<TextView>(R.id.txtServicesTitle)
        private val description = view.findViewById<TextView>(R.id.txtServicesDescription)

        init {
            itemView.setOnClickListener {
                listener?.invoke(list[absoluteAdapterPosition].title)
            }
        }

        fun bind() {
            val data = list[absoluteAdapterPosition]
            imageService.setImageResource(data.imgService)
            title.text = data.title
            description.text = data.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}