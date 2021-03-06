package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.items.CardBgItem
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.putArguments

class CardBackgroundsPagerAdapter(
    private val bgsList: List<Int>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = bgsList.size

    override fun createFragment(position: Int): Fragment =
        CardBgItem().putArguments { putInt("BG", bgsList[position]) }
}