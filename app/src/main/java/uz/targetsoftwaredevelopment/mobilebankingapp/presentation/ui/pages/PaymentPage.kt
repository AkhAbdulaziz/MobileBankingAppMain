package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.SavedPaymentData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.PagePaymentBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.LocationPaymentsAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.MyHomesAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.PaymentForServicesAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.SavedPaymentsAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.*

@AndroidEntryPoint
class PaymentPage : Fragment(R.layout.page_payment) {
    private val binding by viewBinding(PagePaymentBinding::bind)

    private val savedPaymentsAdapter by lazy { SavedPaymentsAdapter(savedPaymentsList) }
    private val paymentForServicesAdapter by lazy { PaymentForServicesAdapter(paymentForServicesList) }
    private val myHomesAdapter by lazy { MyHomesAdapter(myHomesList) }
    private val locationPaymentsAdapter by lazy { LocationPaymentsAdapter(locationPaymentsList) }

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
                    backPressedListener?.invoke(2)
                }
            })

        searchView.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFrom(requireContext(), view)
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
                },
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        // LocationPaymentsAdapter
        rvLocationPayments.adapter = locationPaymentsAdapter
        rvLocationPayments.layoutManager = LinearLayoutManager(requireContext())
        locationPaymentsAdapter.setLocationOnClickListener {
            showFancyToast(
                it,
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }
        locationPaymentsAdapter.setErrorListener {
            showFancyToast(
                it,
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }
    }
}