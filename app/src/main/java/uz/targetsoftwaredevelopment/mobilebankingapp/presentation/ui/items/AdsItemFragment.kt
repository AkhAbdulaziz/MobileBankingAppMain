package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.items

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.AdData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ItemAdsBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

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