package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.dialog.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.DialogChangeBirthdayBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.months
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import java.util.*

@AndroidEntryPoint
class ChangeBirthdayDialog(
    private val currentDay: Int,
    private val currentMonth: Int,
    private val currentYear: Int
) : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogChangeBirthdayBinding::bind)

    private var saveButtonClickListener: ((Int, Int, Int) -> Unit)? = null
    fun setSaveButtonClickListener(block: (Int, Int, Int) -> Unit) {
        saveButtonClickListener = block
    }

    private var cc = Calendar.getInstance(TimeZone.getTimeZone("UTC+5"))
    private var TODAYS_YEAR = cc[Calendar.YEAR]
    private var TODAYS_MONTH = cc[Calendar.MONTH] + 1
    private var TODAYS_DAY = cc[Calendar.DAY_OF_MONTH] + 1

    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

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
        return inflater.inflate(R.layout.dialog_change_birthday, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        dayPicker.value = selectedDay
        monthPicker.value = selectedMonth
        yearPicker.value = selectedYear

        val days = ArrayList<String>()
        for (i in 1 until 32) {
            if (i < 10) {
                days.add("0$i")
            } else {
                days.add("$i")
            }
        }

        dayPicker.apply {
//            wrapSelectorWheel = false
            minValue = 1
            maxValue = 31
            value = selectedDay
            displayedValues = days.toTypedArray()
            setOnValueChangedListener { picker, oldVal, newVal ->
                selectedDay = newVal
                checkDate()
            }
        }

        monthPicker.apply {
//            wrapSelectorWheel = false
            minValue = 1
            maxValue = 12
            value = selectedMonth
            displayedValues = months
            setOnValueChangedListener { picker, oldVal, newVal ->
                selectedMonth = newVal
                checkDate()
            }
        }

        yearPicker.apply {
            wrapSelectorWheel = false
            minValue = 1
            maxValue = TODAYS_YEAR
            value = selectedYear
            setOnValueChangedListener { picker, oldVal, newVal ->
                selectedYear = newVal
                checkDate()
            }
        }

        saveBtn.setOnClickListener {
            saveButtonClickListener?.invoke(selectedDay, selectedMonth, selectedYear)
            dismiss()
        }

        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun checkDate() {
        if (isSmthNew()) {
            if (isTodayLastDayOfMonth()) {
                binding.saveBtn.isEnabled =
                    !(selectedYear == TODAYS_YEAR && selectedMonth == TODAYS_MONTH && selectedDay == 1)
            } else {
                binding.saveBtn.isEnabled =
                    !((selectedYear == TODAYS_YEAR && selectedMonth == TODAYS_MONTH && selectedDay > TODAYS_DAY) || (selectedYear == TODAYS_YEAR && selectedMonth > TODAYS_MONTH))
            }
        } else {
            binding.saveBtn.isEnabled = false
        }
    }

    private fun isTodayLastDayOfMonth(): Boolean {
        return (TODAYS_DAY == 31 && months31.contains(months[TODAYS_MONTH])) || (TODAYS_DAY == 30 && months30.contains(
            months[TODAYS_MONTH]
        )) || (if (isYearOfLeap()) {
            TODAYS_DAY == 29 && months28or29.contains(months[TODAYS_MONTH])
        } else {
            TODAYS_DAY == 28 && months28or29.contains(months[TODAYS_MONTH])
        })
    }

    private val months28or29 = arrayOf("February")
    private val months30 = arrayOf("September", "April", "June", "November")
    private val months31 =
        arrayOf("January", "March", "May", "July", "August", "October", "December")

    private fun isYearOfLeap(): Boolean {
        val year = TODAYS_YEAR
        var leap = false

        if (year % 4 == 0) {
            if (year % 100 == 0) {
                // year is divisible by 400, hence the year is a leap year
                leap = year % 400 == 0
            } else {
                leap = true
            }
        } else {
            leap = false
        }

        return leap
    }

    private fun isSmthNew(): Boolean {
        return (selectedDay != currentDay) || (selectedMonth != currentMonth) || (selectedYear != currentYear)
    }
}