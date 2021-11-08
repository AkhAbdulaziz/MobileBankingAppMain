package uz.gita.mobilebankingapp.presentation.ui.pages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.PageMapBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class MapPage : Fragment(R.layout.page_map) {
    private val binding by viewBinding(PageMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {

    }
}