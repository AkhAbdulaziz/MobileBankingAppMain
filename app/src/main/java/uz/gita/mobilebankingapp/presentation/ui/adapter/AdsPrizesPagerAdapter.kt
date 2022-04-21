package uz.gita.mobilebankingapp.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.mobilebankingapp.presentation.ui.screens.card.CardBgFragment
import uz.gita.mobilebankingapp.utils.putArguments

class AdsPrizesPagerAdapter(
    private val adsList: List<Int>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = adsList.size

    override fun createFragment(position: Int): Fragment =
        CardBgFragment().putArguments { putInt("AD_PRIZES", adsList[position]) }
}