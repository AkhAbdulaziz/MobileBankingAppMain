package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.data.enums.StartScreenEnum
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.gita.mobilebankingapp.databinding.ScreenBasicNavBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.BasicScreenAdapter
import uz.gita.mobilebankingapp.presentation.ui.dialog.auth.ClarifyLogoutDialog
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.BasicViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.BasicViewModelImpl
import uz.gita.mobilebankingapp.utils.invisible
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class BasicScreen : Fragment(R.layout.screen_basic_nav),
    NavigationView.OnNavigationItemSelectedListener {
    private val binding by viewBinding(ScreenBasicNavBinding::bind)
    private val viewModel: BasicViewModel by viewModels<BasicViewModelImpl>()
    private val args: BasicScreenArgs by navArgs()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        viewModel.getProfileInfo()

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        txtUserName
        val adapter = BasicScreenAdapter(childFragmentManager, lifecycle, args.lastScreen)
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
        adapter.apply {
            setOnClickHomeButtonListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        textFullName.apply {
            setOnClickListener {
                viewModel.openProfileScreen()
            }
        }

        noProfilePreview.setOnClickListener {
            viewModel.openProfileScreen()
        }

        viewPaymePeople.setOnClickListener {
            findNavController().navigate(BasicScreenDirections.actionBasicScreenToPaymePeopleScreen())
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
        viewModel.profileInfoLiveData.observe(viewLifecycleOwner, profileInfoObserver)
        viewModel.openLoginScreenLiveData.observe(viewLifecycleOwner, openLoginScreenObserver)
        viewModel.logoutResponseLiveData.observe(viewLifecycleOwner, logoutResponseObserver)
    }

    private val openLoginScreenObserver = Observer<LogoutResponse> {
        findNavController().navigate(
            BasicScreenDirections.actionBasicScreenToPinCodeScreen(
                StartScreenEnum.MAIN
            )
        )
    }

    private val logoutResponseObserver = Observer<LogoutResponse> {
        findNavController().navigate(
            BasicScreenDirections.actionBasicScreenToPinCodeScreen(
                StartScreenEnum.MAIN, false
            )
        )
    }

    private val profileInfoObserver = Observer<ProfileInfoResponse> {
        val profileData = it.data!!
        binding.scope {
            Log.d(
                "PROFILE_my",
                "firstname = ${profileData.firstName} lastname = ${profileData.lastName}"
            )
            if ((profileData.firstName == null || profileData.firstName.isEmpty()) && (profileData.lastName == null || profileData.lastName.isEmpty())) {
                textFullName.invisible()
                layoutUsername.invisible()
                noProfilePreview.visible()
            } else if (profileData.username.isEmpty() || profileData.username == "Fill in the nickname") {
                noProfilePreview.invisible()
                textFullName.text = "${profileData!!.firstName} ${profileData!!.lastName}"
                textFullName.visible()
                layoutUsername.invisible()
            } else {
                noProfilePreview.invisible()
                textFullName.text = "${profileData!!.firstName} ${profileData!!.lastName}"
                textFullName.visible()
                txtUserName.text = profileData.username
                layoutUsername.visible()
            }
        }
    }

    private val openProfileScreenObserver = Observer<Unit> {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        findNavController().navigate(R.id.action_basicScreen_to_profileScreen)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = true
}