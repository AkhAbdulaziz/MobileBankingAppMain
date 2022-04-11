package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.databinding.ScreenProfileSettingsBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.ProfileSettingsViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.ProfileSettingsViewModelImpl
import uz.gita.mobilebankingapp.utils.invisible
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.timber
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class ProfileSettings : Fragment(R.layout.screen_profile_settings) {
    private val binding by viewBinding(ScreenProfileSettingsBinding::bind)
    private val viewModel: ProfileSettingsViewModel by viewModels<ProfileSettingsViewModelImpl>()
    private var firstName: String = ""
    private var lastName: String = ""
    private var nickname: String = ""
    private var birthday: String = ""
    private var gender: String = ""
    private var phone1: String = ""
    private var phone2: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserLocalData()

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        phoneNumber2CardView.setOnClickListener {
            if (phoneNumber2Preview.isVisible) {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            } else {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            }
        }
        phoneNumber2CardView.setOnFocusChangeListener { view, b ->
            if (!b) {
                if (!phoneNumber2Preview.isVisible) {
                    phoneNumber2EditTextLayout.visible()
                    phoneNumber2Preview.invisible()
                } else {
                    phoneNumber2Preview.visible()
                    phoneNumber2EditTextLayout.invisible()
                }
            }
        }

        genderCardView.setOnClickListener {

        }
        birthdayLayout.setOnClickListener {

        }

        btnSaveChanges.setOnClickListener {
            timber("DataSaved Screen", "DATA_SAVED_00")
            viewModel.setUserLocalData(
                UserLocalData(
                    firstNameEditText.text.toString(),
                    lastNameEditText.text.toString(),
                    nickNameEditText.text.toString(),
                    phoneNumber1EditText.text.toString(),
                    phoneNumber2EditText.text.toString(),
                    txtGender.text.toString(),
                    txtBirthday.text.toString()
                )
            )
            findNavController().popBackStack()
        }

        viewModel.userLocalDataLiveData.observe(viewLifecycleOwner, userLocalDataObserver)
        viewModel.profileInfoLiveData.observe(viewLifecycleOwner, profileInfoObserver)
        viewModel.userLocalDataSavedLiveData.observe(viewLifecycleOwner, userLocalDataSavedObserver)
    }

    private val userLocalDataObserver = Observer<UserLocalData> { userLocalData ->
        binding.firstNameEditText.setText("${userLocalData.firstName}")
        binding.lastNameEditText.setText("${userLocalData.lastName}")
        binding.txtBirthday.text = "${userLocalData.birthday}"
        binding.nickNameEditText.setText("${userLocalData.nickname}")
        binding.phoneNumber1EditText.setText("${userLocalData.phoneNumber1}")
        if (userLocalData.phoneNumber2.isNotEmpty()) {
            binding.phoneNumber2EditTextLayout.visible()
        }
        binding.phoneNumber2EditText.setText("${userLocalData.phoneNumber2}")
        binding.txtGender.text = "${userLocalData.gender}"

        firstName = "${userLocalData.firstName}"
        lastName = "${userLocalData.lastName}"
        birthday = "${userLocalData.birthday}"
        nickname = "${userLocalData.nickname}"
        phone1 = "${userLocalData.phoneNumber1}"
        phone2 = "${userLocalData.phoneNumber2}"
        gender = "${userLocalData.gender}"

        viewModel.getProfileInfo()
    }

    private val profileInfoObserver = Observer<ProfileInfoResponse>() {
        val profileData = it.data
        binding.firstNameEditText.setText("${profileData!!.firstName}")
        binding.lastNameEditText.setText("${profileData.lastName}")
        binding.phoneNumber1EditText.setText("${profileData.phone}")
        binding.progressBar.invisible()
    }

    private val userLocalDataSavedObserver = Observer<Unit> {
        timber("DataSaved InsideObserver", "DATA_SAVED_00")
        binding.progressBar.invisible()
    }
}
