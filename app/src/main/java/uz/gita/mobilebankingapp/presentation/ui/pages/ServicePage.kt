package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.PageServiceBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class ServicePage : Fragment(R.layout.page_service) {
    private val binding by viewBinding(PageServiceBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {

    }
}