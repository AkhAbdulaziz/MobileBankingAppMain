package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import uz.gita.mobilebankingapp.presentation.dialog.auth.ClarifyLogoutDialog
import uz.gita.mobilebankingapp.presentation.ui.adapter.BasicScreenAdapter
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
        val adapter = BasicScreenAdapter(childFragmentManager, lifecycle)
        innerLayout.pager.adapter = adapter
        innerLayout.pager.isUserInputEnabled = false

        innerLayout.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> innerLayout.pager.setCurrentItem(0, false)
                R.id.transfer -> innerLayout.pager.setCurrentItem(1, false)
                R.id.payment -> innerLayout.pager.setCurrentItem(2, false)
                R.id.services -> innerLayout.pager.setCurrentItem(3, false)
                else -> innerLayout.pager.setCurrentItem(4, false)
            }
            return@setOnItemSelectedListener true
        }
        adapter.setOnClickHomeButtonListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            drawerLayout.openDrawer(GravityCompat.START)
            Log.d("HOME_BTN", "basic screenda bosildi")
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
        lineDescriptions.setOnClickListener {
            findNavController().navigate(BasicScreenDirections.actionBasicScreenToDescriptionsScreen())
        }
        lineLogout.setOnClickListener {
            val clarifyLogoutDialog = ClarifyLogoutDialog()
            clarifyLogoutDialog.setPositiveBtnListener {
                viewModel.logout()
            }
            clarifyLogoutDialog.show(childFragmentManager, "logout_dialog")
        }

        viewModel.openProfileScreenLiveData.observe(this@BasicScreen, openProfileScreenObserver)
    }

    private val openProfileScreenObserver = Observer<Unit> {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        findNavController().navigate(R.id.action_basicScreen_to_profileScreen)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = true
}