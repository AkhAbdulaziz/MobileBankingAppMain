package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.databinding.ScreenBasicNavBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.BasicPageAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.BasicViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.BasicViewModelImpl
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class BasicScreen : Fragment(R.layout.screen_basic_nav),
    NavigationView.OnNavigationItemSelectedListener {
    private val binding by viewBinding(ScreenBasicNavBinding::bind)
    private val viewModel: BasicViewModel by viewModels<BasicViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        val adapter = BasicPageAdapter(childFragmentManager, lifecycle)
        innerLayout.pager.adapter = adapter
        innerLayout.pager.isUserInputEnabled = false

        innerLayout.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main -> innerLayout.pager.currentItem = 0
                R.id.payment -> innerLayout.pager.currentItem = 1
                R.id.map -> innerLayout.pager.currentItem = 2
                R.id.service -> innerLayout.pager.currentItem = 3
                else -> innerLayout.pager.currentItem = 4
            }
            return@setOnItemSelectedListener true
        }
        adapter.setOnClickHomeButtonListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        textUserName.text = "John Smith"
        textPhoneNumber.text = viewModel.getUserPhoneNumber()

        switchToProfileVew.setOnClickListener {
            viewModel.openProfileScreen()
        }

        lineSettings.setOnClickListener {
            showToast("Settings")
        }
        lineTheme.setOnClickListener {
            showToast("Theme")
        }
        lineShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val link =
                "Pay quickly!: https://play.google.com/store/apps/details?id=${App.instance.packageName}"
            intent.putExtra(Intent.EXTRA_TEXT, link)
            requireActivity().startActivity(Intent.createChooser(intent, "Share:"))
        }
        lineSupport.setOnClickListener {
            showToast("Support")
        }
        lineLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.openProfileScreenLiveData.observe(this@BasicScreen, openProfileScreenObserver)
    }

    private val openProfileScreenObserver = Observer<Unit> {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        findNavController().navigate(R.id.action_basicScreen_to_profileScreen)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = true

}