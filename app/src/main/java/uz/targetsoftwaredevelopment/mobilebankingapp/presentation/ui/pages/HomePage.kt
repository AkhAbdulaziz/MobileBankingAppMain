package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.pages

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager2.widget.MarginPageTransformer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blikoon.qrcodescanner.QrCodeActivity
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.CardData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.PageHomeBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.*
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.pages.MainPageViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.pages.MainPageViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.*
import java.util.*

@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {
    private val binding by viewBinding(PageHomeBinding::bind)
    private val viewModel: MainPageViewModel by viewModels<MainPageViewModelImpl>()
    private val REQUEST_CODE_QR_SCAN = 101

    private val savedPaymentsAdapter by lazy { SavedPaymentsAdapter(savedPaymentsList) }
    private lateinit var adsAdapter: AdsAdapter
    private val paymentForServicesAdapter by lazy { PaymentForServicesAdapter(paymentForServicesList) }
    private val myHomesAdapter by lazy { MyHomesAdapter(myHomesList) }
    private val recentTransactionsAdapter by lazy { RecentTransactionsAdapter(recentTransactionsList) }

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

    private var backPressedListener: ((Int) -> Unit)? = null
    fun setBackPressedListener(f: (Int) -> Unit) {
        backPressedListener = f
    }

    private var openSavedPaymentScreenListener: ((SavedPaymentData) -> Unit)? = null
    fun setOpenSavedPaymentScreenListener(f: (SavedPaymentData) -> Unit) {
        openSavedPaymentScreenListener = f
    }

    private var openNewSavedPaymentScreenListener: (() -> Unit)? = null
    fun setOpenNewSavedPaymentScreenListener(f: () -> Unit) {
        openNewSavedPaymentScreenListener = f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressedListener?.invoke(0)
                }
            })

        viewModel.getTotalSumFromLocal()
        viewModel.getHistoryPagingData()

        arguments?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val isNewUser = it.getBoolean("IS_NEW_USER")
                if (isNewUser) {
                    showAddCardView()
                } else {
                    viewModel.getAllCardList()
                }
            }
        }

        // SavedPaymentsAdapter
        rvSavedPayments.adapter = savedPaymentsAdapter
        rvSavedPayments.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        savedPaymentsAdapter.setSavedPaymentOnClickListener {
            openSavedPaymentScreenListener?.invoke(it)
        }
        savedPaymentsAdapter.setNewButtonOnClickListener {
            openNewSavedPaymentScreenListener?.invoke()
        }

        // AdsAdapter
        adsAdapter = AdsAdapter(requireActivity(), adsList)
        pagerAds.apply {
            adapter = adsAdapter
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2  // make sure left/right item is rendered
        }
        // increase this offset to show more of left/right
        val offsetPx = 16.dpToPx(resources.displayMetrics)
        pagerAds.setPadding(offsetPx, 0, offsetPx, 0)

        // increase this offset to increase distance between 2 items
        val pageMarginPx = 2.dpToPx(resources.displayMetrics)
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        pagerAds.setPageTransformer(marginTransformer)

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                delay(4000L)
                if (pagerAds.currentItem == adsList.size - 1) {
                    pagerAds.setCurrentItem(0, false)
                } else {
                    pagerAds.currentItem += 1
                }
            }
        }

        // PaymentForServicesAdapter
        rvPaymentForServices.adapter = paymentForServicesAdapter
        rvPaymentForServices.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        paymentForServicesAdapter.setServiceOnClickListener { serviceName ->
            showFancyToast(
                serviceName,
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        // MyHomesAdapter
        rvMyHomes.adapter = myHomesAdapter
        rvMyHomes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvMyHomes)

        myHomesAdapter.setHomeOnClickListener {
            showFancyToast(
                if (it.isLast) {
                    "Add new home"
                } else {
                    it.name
                }
            )
        }

        // RecentTransactionsAdapter
        rvRecentTransactions.adapter = recentTransactionsAdapter
        rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
        recentTransactionsAdapter.setTransactionOnClickListener {
            showFancyToast("$it")
        }

        if (viewModel.isBalanceVisible) {
            showBalance()
        } else {
            hideBalance()
        }

        imgEye.setOnClickListener {
            if (viewModel.isBalanceVisible) {
                hideBalance()
            } else {
                showBalance()
            }
        }

        btnHome.setOnClickListener {
            clickHomeButtonListener?.invoke()
            timber("home pageds bosildi", "HOME_BTN")
        }

        btnBell.setOnClickListener {
            showFancyToast(
                "News",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        txtAddCardOrOpenAWallet.setOnClickListener {
            openAddCardScreenListener?.invoke()
        }

        myCardsImg.setOnClickListener {
            openMyCardsScreenListener?.invoke()
        }

        paymeGoImg.setOnClickListener {
            showFancyToast("Payme Go")
        }

        qrPaymentImg.setOnClickListener {
            Permissions.check(requireContext(), arrayOf(
                android.Manifest.permission.CAMERA
            ), null, null,
                object : PermissionHandler() {
                    override fun onGranted() {
                        val i = Intent(requireContext(), QrCodeActivity::class.java)
                        startActivityForResult(i, REQUEST_CODE_QR_SCAN)
                    }
                }
            )
        }

        btnSendMoney.setOnClickListener {
            Log.d("OpenTransferPage", "HomePage")
            openTransferPageListener?.invoke(null)
        }
        btnRequestMoney.setOnClickListener {
            showFancyToast("Request Money")
        }

        viewModel.totalSumFromLocalLiveData.observe(viewLifecycleOwner, totalSumFromLocalObserver)
        viewModel.totalSumLiveData.observe(viewLifecycleOwner, totalSumObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
        viewModel.cardsListLiveData.observe(viewLifecycleOwner, cardsListObserver)
        viewModel.expendituresLiveData.observe(viewLifecycleOwner, expendituresObserver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            timber("COULD NOT GET A GOOD RESULT", "QRCODESCANE")
            if (data == null) return
            //Getting the passed result
            val result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image")
            if (result != null) {
                val alertDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
                alertDialog.setTitle("Scan Error")
                alertDialog.setMessage("QR Code could not be scanned")
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                alertDialog.show()
            }
            return
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null) return
            //Getting the passed result
            val result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult")
            timber("Have scan result in your app activity :$result", "QRCODESCANE")
            val alertDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
            alertDialog.setTitle("Scan result")
            alertDialog.setMessage(result)
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()
        }
    }

    private val totalSumFromLocalObserver = Observer<String> {
        binding.balanceText.text = it
        viewModel.getTotalSum()
    }

    private val totalSumObserver = Observer<String> {
        binding.balanceText.text = it
        binding.progressBar.invisible()
    }

    private val errorMessageObserver = Observer<String> {
        showFancyToast(
            it,
            FancyToast.LENGTH_SHORT,
            FancyToast.ERROR
        )
        binding.progressBar.invisible()
    }

    private val cardsListObserver = Observer<List<CardData>> {
        binding.scope {
            if (it.isEmpty()) {
                showAddCardView()
            } else {
                hideAddCardView()
            }
        }
    }

    private val expendituresObserver = Observer<String> {
        binding.expensesText.text =
            "Expenditure for ${months[Calendar.getInstance(TimeZone.getTimeZone("UTC+5"))[Calendar.MONTH]]} -$it sum"
    }

    private fun showAddCardView() = binding.scope {
        txtAddCardOrOpenAWallet.visible()
        imgEye.invisible()
        balanceTitle.invisible()
        balanceText.invisible()
        currencyText.invisible()
        expensesText.invisible()
    }

    private fun hideAddCardView() = binding.scope {
        txtAddCardOrOpenAWallet.invisible()
        imgEye.visible()
        balanceTitle.visible()
        balanceText.visible()
        currencyText.visible()
        expensesText.visible()
    }

    private fun hideBalance() = binding.scope {
        imgEye.setImageResource(R.drawable.ic_eye_opened)
        expensesText.gone()
        balanceTitle.invisible()
        currencyText.gone()
        balanceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        balanceText.setMarginsInDp(0f, 0f, 0f, 0f)
        balanceText.text = "SHOW BALANCE"
        viewModel.isBalanceVisible = false
    }

    private fun showBalance() = binding.scope {
        imgEye.setImageResource(R.drawable.ic_eye_closed)
        balanceTitle.visible()
        expensesText.visible()
        currencyText.visible()
        balanceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f)
        balanceText.setMarginsInDp(0f, 0f, 28f, 0f)
        balanceText.text = "131 202"
        viewModel.isBalanceVisible = true
    }
}
