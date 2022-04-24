package uz.gita.mobilebankingapp.presentation.ui.adapter.items

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ItemCardBgBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class CardBgItem : Fragment(R.layout.item_card_bg) {
    private val binding by viewBinding(ItemCardBgBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        var bg = R.drawable.card_theme1
        arguments?.let {
            bg = it.getInt("BG")
        }
        cardBg.setImageResource(bg)
    }
}