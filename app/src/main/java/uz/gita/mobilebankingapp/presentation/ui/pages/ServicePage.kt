package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.ServicesData
import uz.gita.mobilebankingapp.databinding.PageServiceBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.ServicesAdapter
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class ServicePage : Fragment(R.layout.page_service) {
    private val binding by viewBinding(PageServiceBinding::bind)
    private val servicesList = ArrayList<ServicesData>()
    private val servicesAdapter by lazy { ServicesAdapter(servicesList) }
    private var isFirstTime: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {

        if (isFirstTime) {
            fillServicesList()
        }

        rvServices.adapter = servicesAdapter
        rvServices.layoutManager = LinearLayoutManager(requireContext())
        servicesAdapter.setListener {

        }
    }

    private fun fillServicesList() {
        servicesList.apply {
            add(
                ServicesData(
                    R.drawable.img_service1,
                    getString(R.string.ru_services_title_payme_avia),
                    getString(R.string.ru_services_description_payme_avia)
                )
            )
            add(
                ServicesData(
                    R.drawable.img_service2,
                    getString(R.string.ru_services_title_payment_for_checking_account),
                    getString(R.string.ru_services_description_payment_for_checking_account)
                )
            )
            add(
                ServicesData(
                    R.drawable.img_service3,
                    getString(R.string.ru_services_title_iib_debt_checking),
                    getString(R.string.ru_services_description_iib_debt_checking)
                )
            )
            add(
                ServicesData(
                    R.drawable.img_service4,
                    getString(R.string.ru_services_title_gold_crown),
                    getString(R.string.ru_services_description_gold_crown)
                )
            )
            add(
                ServicesData(
                    R.drawable.img_service5,
                    getString(R.string.ru_services_title_payment_monitoring),
                    getString(R.string.ru_services_description_payment_monitoring)
                )
            )
            add(
                ServicesData(
                    R.drawable.img_service6,
                    getString(R.string.ru_services_title_traffic_fine_notices),
                    getString(R.string.ru_services_description_traffic_fine_notices)
                )
            )
            add(
                ServicesData(
                    R.drawable.img_service7,
                    getString(R.string.ru_services_title_auto_insurance),
                    getString(R.string.ru_services_description_auto_insurance)
                )
            )
        }
    }
}