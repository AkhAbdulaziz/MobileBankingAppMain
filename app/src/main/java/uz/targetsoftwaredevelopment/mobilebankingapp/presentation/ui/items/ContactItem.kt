package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.items

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ItemContactBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope

@AndroidEntryPoint
class ContactItem : Fragment(R.layout.item_contact) {
    private val binding by viewBinding(ItemContactBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)


    }
}