package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.entities.UserLocalData
import uz.gita.mobilebankingapp.data.remote.profile_req_res.response.ProfileInfoResponse
import uz.gita.mobilebankingapp.databinding.ScreenProfileBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.base.main.ProfileViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.main.ProfileViewModelImpl
import uz.gita.mobilebankingapp.utils.*

@AndroidEntryPoint
class ProfileScreen : Fragment(R.layout.screen_profile) {
    private val binding by viewBinding(ScreenProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels<ProfileViewModelImpl>()

    //    private var imageUri: Uri? = null
    private var firstName: String = ""
    private var lastName: String = ""
    private var nickname: String = ""
    private var birthday: String = ""
    private var gender: String = ""
    private var phone1: String = ""
    private var phone2: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
//        setCurrentData()
        viewModel.getUserLocalData()

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        /*userImage.setOnClickListener {
            Permissions.check(requireContext(), arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), null, null,
                object : PermissionHandler() {
                    override fun onGranted() {
                        ImagePicker.with(requireActivity())
                            .cropSquare()
                            .compress(1024)
                            .maxResultSize(
                                1080,
                                1080
                            )
                            .createIntent { intent ->
                                startForProfileImageResult.launch(intent)
                            }
                    }
                }
            )
        }*/

//        viewModel.imageChangedLiveData.observe(viewLifecycleOwner, imageChangedObserver)

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
//            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }
        clickViewChangeAccount.setOnClickListener {
//            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }
        clickViewDeleteAccount.setOnClickListener {
//            findNavController().navigate(ProfileScreenDirections.actionProfileScreenToProfileSettings())
        }

        viewModel.userLocalDataLiveData.observe(viewLifecycleOwner, userLocalDataObserver)
        viewModel.profileInfoLiveData.observe(viewLifecycleOwner, profileInfoObserver)
        viewModel.userLocalDataSavedLiveData.observe(viewLifecycleOwner, userLocalDataSavedObserver)
    }

    private val userLocalDataObserver = Observer<UserLocalData> { userLocalData ->
        binding.userFullName.text = "${userLocalData.firstName} ${userLocalData.lastName}"
        binding.userBirthday.text = "${userLocalData.birthday}"
        binding.txtUserName.text = "${userLocalData.nickname}"
        binding.txtPhoneNumber1.text = "${userLocalData.phoneNumber1}"
        binding.txtPhoneNumber2.text = "${userLocalData.phoneNumber2}"
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

    private val profileInfoObserver = Observer<ProfileInfoResponse> {
        val profileData = it.data
        binding.userFullName.text = "${profileData!!.firstName} ${profileData!!.lastName}"
        binding.txtPhoneNumber1.text = "${profileData.phone}"

        binding.progressBar.invisible()

        firstName = "${profileData.firstName}"
        lastName = "${profileData.lastName}"
        phone1 = "${profileData.phone}"

        viewModel.setUserLocalData(
            UserLocalData(
                firstName,
                lastName,
                nickname,
                phone1,
                phone2,
                gender,
                birthday
            )
        )
    }

    private val userLocalDataSavedObserver = Observer<Unit> {
        timber("DataSavedProfile Screen", "DATA_SAVED_00")
        binding.progressBar.invisible()
    }

    /*  private val startForProfileImageResult =
          registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
              val resultCode = result.resultCode
              val data = result.data

              if (resultCode == Activity.RESULT_OK) {
                  imageUri = data?.data!!

                  if (imageUri != null) {
                      viewModel.setAvatar(imageUri!!)
                  }
              }
          }

      private val imageChangedObserver = Observer<Uri> {
          binding.userImage.setImageURI(it)
      }

      private fun setCurrentData() = binding.scope {
          userImage.setImageURI(imageUri)
      }*/
}
