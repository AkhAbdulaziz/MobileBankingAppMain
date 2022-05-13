package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.UserLocalData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenProfileBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.ProfileViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main.ProfileViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.invisible
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.showFancyToast
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.timber

@AndroidEntryPoint
class ProfileScreen : Fragment(R.layout.screen_profile) {
    private val binding by viewBinding(ScreenProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels<ProfileViewModelImpl>()

    private var firstName: String = "First name"
    private var lastName: String = "Last name"
    private var nickname: String = ""
    private var birthday: String = "Date of Birth"
    private var gender: String = "Does not set"
    private var phone1: String = ""
    private var phone2: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserLocalData()

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        btnEditProfile.setOnClickListener {
            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }
        clickViewProfileSettings1.setOnClickListener {
            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }
        clickViewProfileSettings2.setOnClickListener {
            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }
        clickViewProfileSettings3.setOnClickListener {
            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }
        clickViewProfileSettings4.setOnClickListener {
            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }
        clickViewPaymePeople.setOnClickListener {
            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToPaymePeopleScreen())
        }
        clickViewChangeAccount.setOnClickListener {
            showFancyToast(
                "Change account",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }
        clickViewDeleteAccount.setOnClickListener {
            showFancyToast(
                "Delete account",
                FancyToast.LENGTH_SHORT,
                FancyToast.INFO
            )
        }

        viewModel.userLocalDataLiveData.observe(viewLifecycleOwner, userLocalDataObserver)
        viewModel.profileInfoLiveData.observe(viewLifecycleOwner, profileInfoObserver)
        viewModel.userLocalDataSavedLiveData.observe(viewLifecycleOwner, userLocalDataSavedObserver)
        viewModel.openLoginScreenLiveData.observe(viewLifecycleOwner, openLoginScreenObserver)
    }

    private val userLocalDataObserver = Observer<UserLocalData> { userLocalData ->
        binding.scope {
            userFullName.text = "${userLocalData.firstName} ${userLocalData.lastName}"
            userBirthday.text = "${userLocalData.birthday}"
            txtUserName.text = userLocalData.nickname.ifEmpty { "Fill in the nickname" }
            txtPhoneNumber1.text = "${userLocalData.phoneNumber1}"
            txtPhoneNumber2.text = "${userLocalData.phoneNumber2}"
            txtGender.text = "${userLocalData.gender}"
        }

        firstName = "${userLocalData.firstName}"
        lastName = "${userLocalData.lastName}"
        birthday = "${userLocalData.birthday}"
        nickname = "${userLocalData.nickname}"
        phone1 = "${userLocalData.phoneNumber1}"
        phone2 = "${userLocalData.phoneNumber2}"
        gender = "${userLocalData.gender}"

        viewModel.getProfileInfo()
    }

    private val profileInfoObserver = Observer<ProfileInfoResponse> {
        val profileData = it.data!!
        binding.scope {
            if (profileData.firstName == null && profileData.lastName == null) {
                userFullName.text = "First name Last name"
                firstName = "First name"
                lastName = "Last name"
            } else {
                userFullName.text = "${profileData.firstName} ${profileData.lastName}"
                firstName = "${profileData.firstName}"
                lastName = "${profileData.lastName}"
            }
            txtPhoneNumber1.text = "${profileData.phone}"
        }
        phone1 = "${profileData.phone}"
        binding.progressBar.invisible()
        viewModel.setUserLocalData(
            UserLocalData(
                firstName,
                lastName,
                nickname,
                phone1,
                phone2,
                gender,
                if (birthday == "Date of Birth") "" else birthday
            )
        )
    }

    private val userLocalDataSavedObserver = Observer<Unit> {
        timber("DataSavedProfile Screen", "DATA_SAVED_00")
        binding.progressBar.invisible()
    }

    private val openLoginScreenObserver = Observer<LogoutResponse> {
        findNavController().navigate(
            ProfileScreenDirections.actionProfileScreenToPinCodeScreen(
                StartScreenEnum.MAIN
            )
        )
    }
}
