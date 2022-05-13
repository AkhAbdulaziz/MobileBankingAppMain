package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.PageServiceBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.ServicesAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.servicesList
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast

@AndroidEntryPoint
class ServicePage : Fragment(R.layout.page_service) {
    private val binding by viewBinding(PageServiceBinding::bind)
    private val servicesAdapter by lazy { ServicesAdapter(servicesList) }

    private var backPressedListener: ((Int) -> Unit)? = null
    fun setBackPressedListener(f: (Int) -> Unit) {
        backPressedListener = f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressedListener?.invoke(3)
                }
            })

        rvServices.adapter = servicesAdapter
        rvServices.layoutManager = LinearLayoutManager(requireContext())
        servicesAdapter.setListener {
            showFancyToast(
                it,
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }
    }
}
