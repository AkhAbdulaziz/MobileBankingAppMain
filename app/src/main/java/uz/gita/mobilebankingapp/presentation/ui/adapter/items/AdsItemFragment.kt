package uz.gita.mobilebankingapp.presentation.ui.adapter.items

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.AdData
import uz.gita.mobilebankingapp.databinding.ItemAdsBinding
import uz.gita.mobilebankingapp.utils.scope

class AdsItemFragment : Fragment(R.layout.item_ads) {
    private val binding by viewBinding(ItemAdsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        val bundle = requireArguments()
        val data = bundle.getSerializable("AD_DATA") as AdData

        imgAd.setImageResource(data.image)
        titleAd.text = data.title
        descriptionAd.text = data.description
    }
}