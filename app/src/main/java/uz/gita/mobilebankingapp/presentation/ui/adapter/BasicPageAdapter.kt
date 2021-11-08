package uz.gita.mobilebankingapp.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.mobilebankingapp.presentation.ui.pages.*

class BasicPageAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private var clickHomeButtonListener: (() -> Unit)? = null
    fun setOnClickHomeButtonListener(block: () -> Unit) {
        clickHomeButtonListener = block
    }

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainPage().apply {
                setOnClickHomeButtonListener {
                    clickHomeButtonListener?.invoke()
                }
            }
            1 -> PaymentPage()
            2 -> MapPage()
            3 -> ServicePage()
            else -> HistoryPage()
        }
    }
}