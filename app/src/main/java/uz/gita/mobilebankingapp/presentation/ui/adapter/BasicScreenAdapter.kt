package uz.gita.mobilebankingapp.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.mobilebankingapp.data.enums.PaymentPageEnum
import uz.gita.mobilebankingapp.data.enums.StartScreenEnum
import uz.gita.mobilebankingapp.presentation.ui.pages.*
import uz.gita.mobilebankingapp.utils.putArguments
import uz.gita.mobilebankingapp.utils.timber

class BasicScreenAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val lastScreen: StartScreenEnum
) :
    FragmentStateAdapter(fm, lifecycle) {

    private var clickHomeButtonListener: (() -> Unit)? = null
    fun setOnClickHomeButtonListener(block: () -> Unit) {
        clickHomeButtonListener = block
    }

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        val transferPage = TransferPage()
        val bundle = Bundle()
        bundle.putString("direction_type", PaymentPageEnum.FROM_BASE_SCREEN.name)
        transferPage.arguments = bundle

        return when (position) {
            0 -> HomePage().apply {
                putArguments { putString("LAST_SCREEN_BASIC", lastScreen.name) }

                setOnClickHomeButtonListener {
                    clickHomeButtonListener?.invoke()
                    timber("adapterda bosildi", "HOME_BTN")
                }
            }
            1 -> transferPage
            2 -> PaymentPage()
            3 -> ServicePage()
            else -> HistoryPage()
        }
    }
}