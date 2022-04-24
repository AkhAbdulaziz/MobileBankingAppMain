package uz.gita.mobilebankingapp.presentation.ui.adapter.items

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ItemAdsPrizesPagerBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class AdsPrizesPagerItem : Fragment(R.layout.item_ads_prizes_pager) {
    private val binding by viewBinding(ItemAdsPrizesPagerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        var bg = R.drawable.ad_prizes1
        arguments?.let {
            bg = it.getInt("AD_PRIZES")
        }
        cardBg.setImageResource(bg)
    }
}