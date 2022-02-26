package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenDescriptionsBinding
import uz.gita.mobilebankingapp.utils.scope

class DescriptionsScreen : Fragment(R.layout.screen_descriptions) {
    private val binding by viewBinding(ScreenDescriptionsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope{
        super.onViewCreated(view, savedInstanceState)

        backBtn.setOnClickListener{
            findNavController().popBackStack()
        }
    }
}