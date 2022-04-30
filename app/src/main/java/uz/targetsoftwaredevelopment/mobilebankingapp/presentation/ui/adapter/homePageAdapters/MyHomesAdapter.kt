package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.homePageAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.MyHomeData
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.gone
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.setMarginsInDp
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible

class MyHomesAdapter(private val list: List<MyHomeData>) :
    RecyclerView.Adapter<MyHomesAdapter.VH>() {

    private var homeOnLickListener: ((Int) -> Unit)? = null
    fun setHomeOnClickListener(f: (Int) -> Unit) {
        homeOnLickListener = f
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.txt_name_home)
        private val hiddenName = view.findViewById<TextView>(R.id.txt_hidden_name)
        private val imageAdd = view.findViewById<ImageView>(R.id.img_add_home)
        private val imageEye = view.findViewById<ImageView>(R.id.img_eye)

        init {
            itemView.setOnClickListener {
                homeOnLickListener?.invoke(absoluteAdapterPosition)
                return@setOnClickListener
            }
            imageEye.setOnClickListener {
                list[absoluteAdapterPosition].isNameVisible =
                    !list[absoluteAdapterPosition].isNameVisible
                if (list[absoluteAdapterPosition].isNameVisible) {
                    imageEye.setImageResource(R.drawable.ic_eye_closed)
                    name.visible()
                    hiddenName.gone()
                } else {
                    imageEye.setImageResource(R.drawable.ic_eye_opened)
                    name.gone()
                    hiddenName.visible()
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
                name.text = "Add home"
                imageAdd.visible()
                imageEye.gone()
            } else {
                name.text = data.name
                imageAdd.gone()
                imageEye.visible()
            }
            if (data.isNameVisible) {
                imageEye.setImageResource(R.drawable.ic_eye_closed)
                name.visible()
                hiddenName.gone()
            } else {
                imageEye.setImageResource(R.drawable.ic_eye_opened)
                name.gone()
                hiddenName.visible()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_my_homes, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size
}
