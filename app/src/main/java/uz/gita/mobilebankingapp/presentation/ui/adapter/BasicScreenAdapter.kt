package uz.gita.mobilebankingapp.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.mobilebankingapp.data.enums.PaymentPageEnum
import uz.gita.mobilebankingapp.presentation.ui.pages.*
import uz.gita.mobilebankingapp.utils.timber

class BasicScreenAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private var clickHomeButtonListener: (() -> Unit)? = null
    fun setOnClickHomeButtonListener(block: () -> Unit) {
        clickHomeButtonListener = block
    }

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        val paymentPage = TransferPage()
        val bundle = Bundle()
        bundle.putString("direction_type", PaymentPageEnum.FROM_BASE_SCREEN.name)
        paymentPage.arguments = bundle

        return when (position) {
            0 -> HomePage().apply {
                setOnClickHomeButtonListener {
                    clickHomeButtonListener?.invoke()
                    timber("adapterda bosildi", "HOME_BTN")
                }
            }
            1 -> paymentPage
            2 -> PaymentPage()
            3 -> ServicePage()
            else -> HistoryPage()
        }
    }
}