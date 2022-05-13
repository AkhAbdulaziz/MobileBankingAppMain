package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

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
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.navigation.NavigationView
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.app.App
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenBasicNavBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.BasicScreenAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.auth.ClarifyLogoutDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.BasicViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main.BasicViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.invisible
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.visible

@AndroidEntryPoint
class BasicScreen : Fragment(R.layout.screen_basic_nav),
    NavigationView.OnNavigationItemSelectedListener {
    private val binding by viewBinding(ScreenBasicNavBinding::bind)
    private val viewModel: BasicViewModel by viewModels<BasicViewModelImpl>()
    private val args: BasicScreenArgs by navArgs()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        viewModel.getProfileInfo()

        val adapter = BasicScreenAdapter(childFragmentManager, lifecycle, args.isNewUser)
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
            setBackPressedListener { currentPageId ->
                if (currentPageId == 0) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        requireActivity().finish()
                    }
                } else {
                    innerLayout.pager.setCurrentItem(0, false)
                }
            }
            setOnClickHomeButtonListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            setOpenAddCardScreenListener {
                findNavController().navigate(BasicScreenDirections.actionBasicScreenToAddCardScreen())
            }
            setOpenMyCardsScreenListener {
                findNavController().navigate(BasicScreenDirections.actionBasicScreenToMyCardsScreen())
            }
            setOpenTransferPageListener { pan ->
                Log.d("OpenTransferPage", "BasicScreen")
                if (pan != null) {
                    viewModel.givePanToTransferPage(pan)
                }
                innerLayout.apply {
                    pager.post {
                        pager.setCurrentItem(1, false)
                    }
                    bottomNavigationView.selectedItemId = R.id.transfer
                    Log.d("OpenTransferPage", "InnerLayout")
                }
            }
            setOpenSendMoneyScreenListener { cardNumber, amount, receiverName ->
                findNavController().navigate(
                    BasicScreenDirections.actionBasicScreenToSendMoneyScreen(
                        cardNumber = cardNumber, amount = amount, receiverName = receiverName
                    )
                )
            }
            setOpenPinCodeScreenListener { lastScreen ->
                findNavController().navigate(
                    BasicScreenDirections.actionBasicScreenToPinCodeScreen(
                        lastScreen
                    )
                )
            }
            setOpenSavedPaymentScreenListener {
                findNavController().navigate(
                    BasicScreenDirections.actionBasicScreenToSavedPaymentScreen(
                        it
                    )
                )
            }
            setOpenNewSavedPaymentScreenListener {
                showFancyToast(
                    "Add New Saved Payment",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO
                )
            }
            setOpenContactsScreenListener {
                Permissions.check(requireContext(), arrayOf(
                    android.Manifest.permission.READ_CONTACTS
                ), null, null,
                    object : PermissionHandler() {
                        override fun onGranted() {
                            findNavController().navigate(BasicScreenDirections.actionBasicScreenToContactsScreen())
                        }
                    }
                )
            }
            setOpenScanCardScreenListener {
                findNavController().navigate(BasicScreenDirections.actionBasicScreenToScanCardScreen())
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
            showFancyToast(
                "Settings",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }
        lineTheme.setOnClickListener {
            showFancyToast(
                "Theme",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
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
            showFancyToast(
                "Support",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
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