package uz.gita.mobilebankingapp.presentation.ui.screens

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenProfileBinding
import uz.gita.mobilebankingapp.presentation.viewmodels.ProfileViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.ProfileViewModelImpl
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class ProfileScreen : Fragment(R.layout.screen_profile) {
    private val binding by viewBinding(ScreenProfileBinding::bind)
    private val viewModel: ProfileViewModel by viewModels<ProfileViewModelImpl>()
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        setCurrentData()

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        userImage.setOnClickListener {
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
        }

        viewModel.imageChangedLiveData.observe(viewLifecycleOwner, imageChangedObserver)
    }

    private val imageChangedObserver = Observer<Uri> {
        binding.userImage.setImageURI(it)
    }

    private val startForProfileImageResult =
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

    private fun setCurrentData() = binding.scope {
        userImage.setImageURI(imageUri)
    }
}
