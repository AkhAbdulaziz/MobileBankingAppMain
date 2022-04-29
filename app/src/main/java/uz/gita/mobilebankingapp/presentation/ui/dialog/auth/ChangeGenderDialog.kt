package uz.gita.mobilebankingapp.presentation.ui.dialog.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.DialogChangeGenderBinding
import uz.gita.mobilebankingapp.utils.scope

@AndroidEntryPoint
class ChangeGenderDialog(
    private val currentGender: String
) :  BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogChangeGenderBinding::bind)

    private var saveButtonClickListener: ((String) -> Unit)? = null
    fun setSaveButtonClickListener(block: (String) -> Unit) {
        saveButtonClickListener = block
    }

    private var selectedGender: String = currentGender
    private val genders = arrayOf("Male", "Female")

    override fun onStart() {
        super.onStart()
        //this forces the sheet to appear at max height even on landscape
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_change_gender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            genderPicker.textColor = resources.getColor(R.color.lightBaseColor)
        }*/

        genderPicker.value = if (selectedGender == "Male") 0 else 1

        genderPicker.apply {
            wrapSelectorWheel = false
            minValue = 0
            maxValue = 1
            value = if (selectedGender == "Male") 0 else 1
            displayedValues = genders
            setOnValueChangedListener { picker, oldVal, newVal ->
                selectedGender = genders[newVal]
                checkDate()
            }
        }

        saveBtn.setOnClickListener {
            saveButtonClickListener?.invoke(selectedGender)
            dismiss()
        }

        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun checkDate() {
        binding.saveBtn.isEnabled = isSmthNew()
    }

    private fun isSmthNew(): Boolean {
        return selectedGender != currentGender
    }
}