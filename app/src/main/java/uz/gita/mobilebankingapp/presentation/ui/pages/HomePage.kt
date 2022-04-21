package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager2.widget.MarginPageTransformer
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.*
import uz.gita.mobilebankingapp.data.enums.PaymentPageEnum
import uz.gita.mobilebankingapp.data.enums.TransactionTypes
import uz.gita.mobilebankingapp.databinding.PageHomeBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.homePageAdapters.*
import uz.gita.mobilebankingapp.presentation.viewmodels.base.pages.MainPageViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.pages.MainPageViewModelImpl
import uz.gita.mobilebankingapp.utils.*

@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {
    private val binding by viewBinding(PageHomeBinding::bind)
    private val viewModel: MainPageViewModel by viewModels<MainPageViewModelImpl>()

    private var savedPaymentsList = ArrayList<SavedPaymentData>()
    private val savedPaymentsAdapter by lazy { SavedPaymentsAdapter(savedPaymentsList) }
    private var adsList = ArrayList<AdData>()
    private lateinit var adsAdapter: AdsAdapter
    private var paymentForServicesList = ArrayList<PaymentForServicesData>()
    private val paymentForServicesAdapter by lazy { PaymentForServicesAdapter(paymentForServicesList) }
    private var myHomesList = ArrayList<MyHomeData>()
    private val myHomesAdapter by lazy { MyHomesAdapter(myHomesList) }
    private var recentTransactionsList = ArrayList<RecentTransactionData>()
    private val recentTransactionsAdapter by lazy { RecentTransactionsAdapter(recentTransactionsList) }
    private var isFirstTime: Boolean = true

    private var clickHomeButtonListener: (() -> Unit)? = null
    fun setOnClickHomeButtonListener(block: () -> Unit) {
        clickHomeButtonListener = block
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
//        refresh.isRefreshing = true
        viewModel.getTotalSumFromLocal()

        if (isFirstTime) {
            fillSavedPaymentsList()
            fillAdsList()
            fillPaymentForServicesList()
            fillMyHomesList()
            fillRecentTransactionsList()
            isFirstTime = false
        }

        // SavedPaymentsAdapter
        rvSavedPayments.adapter = savedPaymentsAdapter
        rvSavedPayments.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        savedPaymentsAdapter.setSavedPaymentOnClickListener {
            showToast("Saved Payment $it")
        }
        savedPaymentsAdapter.setNewButtonOnClickListener {
            showToast("New Saved Payment")
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
                    pagerAds.currentItem = 0
                } else {
                    pagerAds.currentItem += 1
                }
            }
        }

        // PaymentForServicesAdapter
        rvPaymentForServices.adapter = paymentForServicesAdapter
        rvPaymentForServices.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        paymentForServicesAdapter.setServiceOnClickListener {
            showToast("Payment for service $it")
        }

        // MyHomesAdapter
        rvMyHomes.adapter = myHomesAdapter
        rvMyHomes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvMyHomes)

        myHomesAdapter.setHomeOnClickListener {
            showToast("My Home $it")
        }

        // RecentTransactionsAdapter
        rvRecentTransactions.adapter = recentTransactionsAdapter
        rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
        recentTransactionsAdapter.setTransactionOnClickListener {
            showToast("Recent Transaction $it")
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        /*refresh.setOnRefreshListener {
            viewModel.getTotalSum()
            refresh.isRefreshing = true
        }*/

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

        homeBtn.setOnClickListener {
            clickHomeButtonListener?.invoke()
            timber("home pageds bosildi", "HOME_BTN")
        }

        myCardsImg.setOnClickListener {
            findNavController().navigate(R.id.action_basicScreen_to_myCardsScreen)
        }

        btnSendMoney.setOnClickListener {
            viewModel.getAllCardList()
            val bundle = Bundle()
            bundle.putString("direction_type", PaymentPageEnum.FROM_PAYMENT_SCREEN.name)
            findNavController().navigate(R.id.action_basicScreen_to_transferPage)
        }
        btnRequestMoney.setOnClickListener {
            showToast("Request Money")
        }

        viewModel.totalSumFromLocalLiveData.observe(viewLifecycleOwner, totalSumFromLocalObserver)
        viewModel.totalSumLiveData.observe(viewLifecycleOwner, totalSumObserver)
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner, errorMessageObserver)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCardList()
    }

    private val totalSumFromLocalObserver = Observer<String> {
        if (viewModel.isBalanceVisible) {
            binding.balanceText.text = it
        }
        viewModel.getTotalSum()
    }

    private val totalSumObserver = Observer<String> {
        if (viewModel.isBalanceVisible) {
            binding.balanceText.text = it
        }
//        binding.refresh.isRefreshing = false
        binding.progressBar.invisible()
    }

    private val errorMessageObserver = Observer<String> {
//        binding.refresh.isRefreshing = false
        binding.progressBar.invisible()
    }

    private fun fillSavedPaymentsList() {
        savedPaymentsList.apply {
            add(
                SavedPaymentData(
                    false,
                    R.drawable.logo_beeline,
                    "My phone",
                    "+998991002030"
                )
            )
            add(
                SavedPaymentData(
                    false,
                    R.drawable.logo_beeline,
                    "Granny's phone",
                    "+998919998877"
                )
            )
            add(
                SavedPaymentData(
                    false,
                    R.drawable.logo_atto,
                    "My ATTO",
                    "14 000 sum"
                )
            )
            add(
                SavedPaymentData(
                    false,
                    R.drawable.logo_mobiuz,
                    "brother",
                    "+998901510051"
                )
            )
            add(
                SavedPaymentData(
                    true,
                    R.drawable.logo_mobiuz,
                    "brother",
                    "+998901510051"
                )
            )
        }
    }

    private fun fillAdsList() {
        adsList.apply {
            add(
                AdData(
                    R.drawable.ad1,
                    getString(R.string.ru_ad1_title),
                    getString(R.string.ru_ad1_description),
                )
            )
            add(
                AdData(
                    R.drawable.ad2,
                    getString(R.string.ru_ad2_title),
                    getString(R.string.ru_ad2_description),
                )
            )
            add(
                AdData(
                    R.drawable.ad3,
                    getString(R.string.ru_ad3_title),
                    getString(R.string.ru_ad3_description),
                )
            )
            add(
                AdData(
                    R.drawable.ad4,
                    getString(R.string.ru_ad4_title),
                    getString(R.string.ru_ad4_description),
                )
            )
            add(
                AdData(
                    R.drawable.ad5,
                    getString(R.string.ru_ad5_title),
                    getString(R.string.ru_ad5_description),
                )
            )
            add(
                AdData(
                    R.drawable.ad6,
                    getString(R.string.ru_ad6_title),
                    getString(R.string.ru_ad6_description),
                )
            )
        }
    }

    private fun fillPaymentForServicesList() {
        paymentForServicesList.apply {
            add(
                PaymentForServicesData(
                    R.drawable.ic_popular,
                    getString(R.string.ru_payment_for_service_popular)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_smartphone,
                    getString(R.string.ru_payment_for_service_mobile_operators)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_internet,
                    getString(R.string.ru_payment_for_service_internet_providers)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_house,
                    getString(R.string.ru_payment_for_service_communal)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_government,
                    getString(R.string.ru_payment_for_service_government)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_phone,
                    getString(R.string.ru_payment_for_service_telephone)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_tv,
                    getString(R.string.ru_payment_for_service_tv)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_online_services,
                    getString(R.string.ru_payment_for_service_online_service)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_poster,
                    getString(R.string.ru_payment_for_service_poster)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_bank_credit,
                    getString(R.string.ru_payment_for_service_bank)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_percentage,
                    getString(R.string.ru_payment_for_service_credit)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_bus,
                    getString(R.string.ru_payment_for_service_transport)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_heart,
                    getString(R.string.ru_payment_for_service_charity)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_hat_graduation,
                    getString(R.string.ru_payment_for_service_education)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_wallet,
                    getString(R.string.ru_payment_for_service_electronic_wallet)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_umbrella,
                    getString(R.string.ru_payment_for_service_insurance)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_game_controller,
                    getString(R.string.ru_payment_for_service_game_social_media)
                )
            )
            add(
                PaymentForServicesData(
                    R.drawable.ic_airplane,
                    getString(R.string.ru_payment_for_service_airplane)
                )
            )
        }
    }

    private fun fillMyHomesList() {
        myHomesList.apply {
            add(
                MyHomeData(
                    false,
                    "My home1",
                    true
                )
            )
            add(
                MyHomeData(
                    false,
                    "My home2",
                    false
                )
            )
            add(
                MyHomeData(
                    true,
                    "My home2",
                    false
                )
            )
        }
    }

    private fun fillRecentTransactionsList() {
        recentTransactionsList.apply {
            add(
                RecentTransactionData(
                    null,
                    "myinfin online\nconvert",
                    "21 February",
                    "00:39",
                    "-5 430",
                    TransactionTypes.CONVERSION
                )
            )
            add(
                RecentTransactionData(
                    null,
                    "myinfin online\nconvert",
                    "21 February",
                    "00:34",
                    "-124 564.2",
                    TransactionTypes.CONVERSION
                )
            )
            add(
                RecentTransactionData(
                    R.drawable.infinbank_logo,
                    "",
                    "21 February",
                    "00:33",
                    "-70 700",
                    TransactionTypes.TRANSFER
                )
            )
        }
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
//        viewModel.getTotalSum()
        balanceText.text = "131 202"
        viewModel.isBalanceVisible = true
    }
}
