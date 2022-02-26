package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.LocationPaymentsData
import uz.gita.mobilebankingapp.data.entities.MyHomeData
import uz.gita.mobilebankingapp.data.entities.PaymentForServicesData
import uz.gita.mobilebankingapp.data.entities.SavedPaymentData
import uz.gita.mobilebankingapp.databinding.PagePaymentBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.homePageAdapters.MyHomesAdapter
import uz.gita.mobilebankingapp.presentation.ui.adapter.homePageAdapters.PaymentForServicesAdapter
import uz.gita.mobilebankingapp.presentation.ui.adapter.homePageAdapters.SavedPaymentsAdapter
import uz.gita.mobilebankingapp.presentation.ui.adapter.paymentPageAdapters.LocationPaymentsAdapter
import uz.gita.mobilebankingapp.utils.hideKeyboardFrom
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class PaymentPage : Fragment(R.layout.page_payment) {
    private val binding by viewBinding(PagePaymentBinding::bind)
    private var savedPaymentsList = ArrayList<SavedPaymentData>()
    private val savedPaymentsAdapter by lazy { SavedPaymentsAdapter(savedPaymentsList) }
    private var paymentForServicesList = ArrayList<PaymentForServicesData>()
    private val paymentForServicesAdapter by lazy { PaymentForServicesAdapter(paymentForServicesList) }
    private var myHomesList = ArrayList<MyHomeData>()
    private val myHomesAdapter by lazy { MyHomesAdapter(myHomesList) }
    private var locationPaymentsList = ArrayList<LocationPaymentsData>()
    private val locationPaymentsAdapter by lazy { LocationPaymentsAdapter(locationPaymentsList) }
    private var isFirstTime: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {

        searchView.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFrom(requireContext(), view)
            }
        }

        if (isFirstTime) {
            fillSavedPaymentsList()
            fillPaymentForServicesList()
            fillMyHomesList()
            fillLocationPaymentsList()
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

        // LocationPaymentsAdapter
        rvLocationPayments.adapter = locationPaymentsAdapter
        rvLocationPayments.layoutManager = LinearLayoutManager(requireContext())
        locationPaymentsAdapter.setLocationOnClickListener {

        }
        locationPaymentsAdapter.setErrorListener {

        }
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

    private fun fillLocationPaymentsList() {
        locationPaymentsList.apply {
            add(
                LocationPaymentsData(
                    null,
                    null,
                    false,
                    R.drawable.logo_c_space,
                    getString(R.string.location_c_space_name),
                    getString(R.string.location_c_space_address),
                    "150 m",
                    "Products",
                    true
                )
            )
            add(
                LocationPaymentsData(
                    null,
                    null,
                    false,
                    R.drawable.logo_candy_dance_studio,
                    getString(R.string.location_candy_dance_studio_name),
                    getString(R.string.location_candy_dance_studio_address),
                    "150 m",
                    null,
                    true
                )
            )
            add(
                LocationPaymentsData(
                    null,
                    null,
                    false,
                    R.drawable.logo_rukkola,
                    getString(R.string.location_rukkola_name),
                    getString(R.string.location_rukkola_address),
                    "150 m",
                    null,
                    false
                )
            )
        }
    }
}