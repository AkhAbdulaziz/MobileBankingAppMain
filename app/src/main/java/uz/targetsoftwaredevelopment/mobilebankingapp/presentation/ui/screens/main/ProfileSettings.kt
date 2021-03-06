package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.UserLocalData
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.StartScreenEnum
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.user_req_res.response.LogoutResponse
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenProfileSettingsBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.auth.ChangeBirthdayDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.auth.ChangeGenderDialog
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.main.ProfileSettingsViewModel
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.impl.main.ProfileSettingsViewModelImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.*
import java.util.*

@AndroidEntryPoint
class ProfileSettings : Fragment(R.layout.screen_profile_settings) {
    private val binding by viewBinding(ScreenProfileSettingsBinding::bind)
    private val viewModel: ProfileSettingsViewModel by viewModels<ProfileSettingsViewModelImpl>()

    private var cc = Calendar.getInstance(TimeZone.getTimeZone("UTC+5"))
    private var TODAYS_YEAR = cc[Calendar.YEAR]
    private var TODAYS_MONTH = cc[Calendar.MONTH] + 1
    private var TODAYS_DAY = cc[Calendar.DAY_OF_MONTH] + 1

    private var firstName: String = ""
    private var lastName: String = ""
    private var nickname: String = ""
    private var birthday: String = ""
    private var gender: String = "Male"
    private var phone1: String = ""
    private var phone2: String = ""

    private var genderChangingEnabled = false
    private var birthdayChangingEnabled = false

    private var isSomethingNew = false
    private var isFirstNameReady = true
    private var isPhoneNumber1Ready = false
    private var isPhoneNumber2Ready = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserLocalData()
        loadSecondPhoneLayoutFocusChanges()

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        firstNameEditText.apply {
            addTextChangedListener {
//                firstNameEditTextLayout.disableError()
                it?.let {
//                    isFirstNameReady = it.isNotEmpty()
                    if (firstName != firstNameEditText.text.toString()) {
                        isSomethingNew = true
                    }
                    check()
                }
            }
            /*setOnFocusChangeListener { view, b ->
                if (!b && firstNameEditText.text.toString().isEmpty()) {
                    firstNameEditTextLayout.enableError()
                    firstNameEditTextLayout.error = "Firstname required"
                }
            }*/
        }

        lastNameEditText.addTextChangedListener {
            it?.let {
                if (lastName != lastNameEditText.text.toString()) {
                    isSomethingNew = true
                }
                check()
            }
        }

        nickNameEditText.addTextChangedListener {
            it?.let {
                if (nickname != nickNameEditText.text.toString()) {
                    isSomethingNew = true
                }
                check()
            }
        }

        phoneNumber1EditText.apply {
            addTextChangedListener {
                phoneNumber1EditTextLayout.disableError()
                it?.let {
                    isPhoneNumber1Ready = it.length == 13 && it.toString().startsWith("+998")
                    if (phone1 != phoneNumber1EditText.text.toString()) {
                        isSomethingNew = true
                    }
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b) {
                    if ((phoneNumber1EditText.text.toString()
                            .isEmpty() || phoneNumber1EditText.text.toString() == "+998")
                    ) {
                        phoneNumber1EditTextLayout.enableError()
                        phoneNumber1EditTextLayout.error = "Phone is required"
                    } else if (!phoneNumber1EditText.text.toString().startsWith("+998")) {
                        phoneNumber1EditTextLayout.enableError()
                        phoneNumber1EditTextLayout.error = "Need to starts with +998"
                    } else if (phoneNumber1EditText.text.toString().length != 13) {
                        phoneNumber1EditTextLayout.enableError()
                        phoneNumber1EditTextLayout.error = "Phone is not valid"
                    } else {
                        phoneNumber1EditTextLayout.disableError()
                    }
                }
            }
        }


        phoneNumber2Layout.setOnClickListener {
            if (phoneNumber2Preview.isVisible) {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            } else {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            }
        }

        phoneNumber2EditText.apply {
            addTextChangedListener {
                phoneNumber2EditTextLayout.disableError()
                it?.let {
                    isPhoneNumber2Ready =
                        ((it.length == 13) && it.toString()
                            .startsWith("+998")) || phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998"
                    if (phone2 != phoneNumber2EditText.text.toString()) {
                        isSomethingNew = true
                    }
                    check()
                }
            }
            setOnFocusChangeListener { view, b ->
                if (!b) {
                    if (phoneNumber2EditText.text.isNotEmpty() && phoneNumber2EditText.text.toString() != "+998") {
                        if (!phoneNumber2EditText.text.toString().startsWith("+998")) {
                            phoneNumber2EditTextLayout.enableError()
                            phoneNumber2EditTextLayout.error = "Need to starts with +998"
                        } else if (phoneNumber2EditText.text.toString().length != 13) {
                            phoneNumber2EditTextLayout.enableError()
                            phoneNumber2EditTextLayout.error = "Phone is not valid"
                        } else {
                            phoneNumber2EditTextLayout.disableError()
                        }
                    } else {
                        if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                            phoneNumber2Preview.visible()
                            phoneNumber2EditTextLayout.invisible()
                        } else {
                            phoneNumber2EditTextLayout.visible()
                            phoneNumber2Preview.invisible()
                        }
                    }
                }
            }
        }

        genderCardView.setOnClickListener {
            if (genderChangingEnabled) {
                val changeGenderDialog =
                    ChangeGenderDialog(gender)

                changeGenderDialog.setSaveButtonClickListener { newGender ->
                    gender = newGender
                    txtGender.text = newGender
                }
                changeGenderDialog.show(childFragmentManager, "GenderChanged")
            }
        }

        birthdayLayout.setOnClickListener {
            if (birthdayChangingEnabled) {
                val currentDay =
                    if (birthday.isNotEmpty()) birthday.substring(0, 2).toInt() else TODAYS_DAY
                val currentMonth =
                    if (birthday.isNotEmpty()) birthday.substring(3, 5).toInt() else TODAYS_MONTH
                val currentYear =
                    if (birthday.isNotEmpty()) birthday.substring(6).toInt() else TODAYS_YEAR

                val changeBirthdayDialog =
                    ChangeBirthdayDialog(currentDay, currentMonth, currentYear)

                changeBirthdayDialog.setSaveButtonClickListener { day, month, year ->
                    birthday = "${day}.${getTrueMonthNumber(month)}.${year}"
                    txtBirthday.text = birthday
                    if (birthday != "${day}.${getTrueMonthNumber(month)}.${year}") {
                        isSomethingNew = true
                    }
                }
                changeBirthdayDialog.show(childFragmentManager, "BirthdayChanged")
            }
        }

        btnSaveChanges.setOnClickListener {
            binding.progressBar.visible()
            viewModel.saveUserData(
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
        }

        viewModel.userLocalDataLiveData.observe(viewLifecycleOwner, userLocalDataObserver)
        viewModel.profileInfoLiveData.observe(viewLifecycleOwner, profileInfoObserver)
        viewModel.userDataSavedLiveData.observe(viewLifecycleOwner, userDataSavedObserver)
        viewModel.openLoginScreenLiveData.observe(viewLifecycleOwner, openLoginScreenObserver)
    }

    private fun check() {
        binding.btnSaveChanges.isEnabled =
            isSomethingNew && isFirstNameReady && isPhoneNumber1Ready && isPhoneNumber2Ready
    }

    private val openLoginScreenObserver = Observer<LogoutResponse> {
        findNavController().navigate(
            ProfileSettingsDirections.actionProfileSettingsToPinCodeScreen(
                StartScreenEnum.MAIN
            )
        )
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
        if (profileData.firstName != null) {
            firstName = profileData.firstName
        }

        binding.lastNameEditText.setText("${profileData.lastName}")
        if (profileData.lastName != null) {
            lastName = profileData.lastName
        }

        binding.phoneNumber1EditText.setText("${profileData.phone}")
        if (profileData.phone != null) {
            phone1 = profileData.phone
        }

        genderChangingEnabled = true
        birthdayChangingEnabled = true
        binding.progressBar.invisible()
    }

    private val userDataSavedObserver = Observer<Unit> {
        binding.progressBar.invisible()
        findNavController().popBackStack()
    }

    private fun loadSecondPhoneLayoutFocusChanges() = binding.scope {
        firstNameEditText.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        lastNameEditText.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        nickNameEditText.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        phoneNumber1EditText.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        topBar.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        genderCardView.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        birthdayLayout.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        btnSaveChanges.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }

        background.setOnClickListener {
            if (phoneNumber2EditText.text.isEmpty() || phoneNumber2EditText.text.toString() == "+998") {
                phoneNumber2Preview.visible()
                phoneNumber2EditTextLayout.invisible()
            } else {
                phoneNumber2EditTextLayout.visible()
                phoneNumber2Preview.invisible()
            }
        }
    }

    private fun getTrueMonthNumber(monthNumber: Int): String {
        return if (monthNumber < 10) {
            "0$monthNumber"
        } else {
            "$monthNumber"
        }
    }
}
