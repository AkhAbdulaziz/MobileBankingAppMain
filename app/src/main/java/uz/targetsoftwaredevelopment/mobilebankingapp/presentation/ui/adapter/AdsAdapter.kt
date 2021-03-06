package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.AdData
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.items.AdsItemFragment
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.putArguments

class AdsAdapter(
    fragmentActivity: FragmentActivity,
    private val list: List<AdData>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = list.size
    override fun createFragment(position: Int): Fragment =
        AdsItemFragment().putArguments { putSerializable("AD_DATA", list[position]) }
}