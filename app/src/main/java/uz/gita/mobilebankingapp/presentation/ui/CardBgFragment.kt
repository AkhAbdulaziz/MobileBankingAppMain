package uz.gita.mobilebankingapp.presentation.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.FragmentCardBgBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class CardBgFragment : Fragment(R.layout.fragment_card_bg) {
   private val binding by viewBinding(FragmentCardBgBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        var bg = R.drawable.card_bg1
        arguments?.let {
            bg = it.getInt("BG")
        }
        cardBg.setImageResource(bg)
    }
}