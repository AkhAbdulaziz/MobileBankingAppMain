package uz.gita.mobilebankingapp.presentation.ui.adapter.homePageAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.mobilebankingapp.data.entities.AdData
import uz.gita.mobilebankingapp.presentation.ui.adapter.items.AdsItemFragment
import uz.gita.mobilebankingapp.utils.putArguments

class AdsAdapter(
    fragmentActivity: FragmentActivity,
    private val list: List<AdData>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = list.size
    override fun createFragment(position: Int): Fragment =
        AdsItemFragment().putArguments { putSerializable("AD_DATA", list[position]) }
}