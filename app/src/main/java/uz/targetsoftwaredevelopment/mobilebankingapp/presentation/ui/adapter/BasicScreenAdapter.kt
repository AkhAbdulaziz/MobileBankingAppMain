package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.PaymentPageEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.pages.*
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.putArguments
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.timber

class BasicScreenAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val isNewUser: Boolean
) : FragmentStateAdapter(fm, lifecycle) {

    private var backPressedListener: ((Int) -> Unit)? = null
    fun setBackPressedListener(f: (Int) -> Unit) {
        backPressedListener = f
    }

    private var clickHomeButtonListener: (() -> Unit)? = null
    fun setOnClickHomeButtonListener(block: () -> Unit) {
        clickHomeButtonListener = block
    }

    private var openAddCardScreenListener: (() -> Unit)? = null
    fun setOpenAddCardScreenListener(block: () -> Unit) {
        openAddCardScreenListener = block
    }

    private var openMyCardsScreenListener: (() -> Unit)? = null
    fun setOpenMyCardsScreenListener(block: () -> Unit) {
        openMyCardsScreenListener = block
    }

    private var openTransferPageListener: ((String?) -> Unit)? = null
    fun setOpenTransferPageListener(block: (String?) -> Unit) {
        openTransferPageListener = block
    }

    private var openSendMoneyScreenListener: ((String, Long, String) -> Unit)? = null
    fun setOpenSendMoneyScreenListener(block: (String, Long, String) -> Unit) {
        openSendMoneyScreenListener = block
    }

    private var openPinCodeScreenListener: ((StartScreenEnum) -> Unit)? = null
    fun setOpenPinCodeScreenListener(block: (StartScreenEnum) -> Unit) {
        openPinCodeScreenListener = block
    }

    private var openSavedPaymentScreenListener: ((SavedPaymentData) -> Unit)? = null
    fun setOpenSavedPaymentScreenListener(f: (SavedPaymentData) -> Unit) {
        openSavedPaymentScreenListener = f
    }

    private var openNewSavedPaymentScreenListener: (() -> Unit)? = null
    fun setOpenNewSavedPaymentScreenListener(f: () -> Unit) {
        openNewSavedPaymentScreenListener = f
    }

    private var openContactsScreenListener: (() -> Unit)? = null
    fun setOpenContactsScreenListener(f: () -> Unit) {
        openContactsScreenListener = f
    }

    private var openScanCardScreenListener: (() -> Unit)? = null
    fun setOpenScanCardScreenListener(f: () -> Unit) {
        openScanCardScreenListener = f
    }


    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        val transferPage = TransferPage()
        transferPage.apply {
            setBackPressedListener { currentPageId ->
                backPressedListener?.invoke(currentPageId)
            }
            setOpenSendMoneyScreenListener { cardNumber, amount, receiverName ->
                openSendMoneyScreenListener?.invoke(cardNumber, amount, receiverName)
            }
            setOpenPinCodeScreenListener {
                openPinCodeScreenListener?.invoke(it)
            }
            setOpenContactsScreenListener {
                openContactsScreenListener?.invoke()
            }
            setOpenScanCardScreenListener {
                openScanCardScreenListener?.invoke()
            }
        }
        val bundle = Bundle()
        bundle.putString("direction_type", PaymentPageEnum.FROM_BASE_SCREEN.name)
        transferPage.arguments = bundle

        return when (position) {
            0 -> HomePage().apply {
                putArguments { putBoolean("IS_NEW_USER", isNewUser) }

                setBackPressedListener { currentPageId ->
                    backPressedListener?.invoke(currentPageId)
                }
                setOnClickHomeButtonListener {
                    clickHomeButtonListener?.invoke()
                    timber("adapterda bosildi", "HOME_BTN")
                }
                setOpenAddCardScreenListener {
                    openAddCardScreenListener?.invoke()
                }
                setOpenMyCardsScreenListener {
                    openMyCardsScreenListener?.invoke()
                }
                setOpenTransferPageListener {
                    Log.d("OpenTransferPage", "Adapter")
                    openTransferPageListener?.invoke(it)
                }
                setOpenSavedPaymentScreenListener {
                    openSavedPaymentScreenListener?.invoke(it)
                }
                setOpenNewSavedPaymentScreenListener {
                    openNewSavedPaymentScreenListener?.invoke()
                }
            }
            1 -> transferPage
            2 -> PaymentPage().apply {
                setBackPressedListener { currentPageId ->
                    backPressedListener?.invoke(currentPageId)
                }
                setOpenSavedPaymentScreenListener {
                    openSavedPaymentScreenListener?.invoke(it)
                }
                setOpenNewSavedPaymentScreenListener {
                    openNewSavedPaymentScreenListener?.invoke()
                }
            }
            3 -> ServicePage().apply {
                setBackPressedListener { currentPageId ->
                    backPressedListener?.invoke(currentPageId)
                }
            }
            else -> HistoryPage().apply {
                setBackPressedListener { currentPageId ->
                    backPressedListener?.invoke(currentPageId)
                }
            }
        }
    }
}