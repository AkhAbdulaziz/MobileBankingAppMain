package uz.gita.mobilebankingapp.presentation.ui.screens.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.databinding.ScreenSucceedMoneySendBinding
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.showToast

@AndroidEntryPoint
class SucceedMoneySendScreen : Fragment(R.layout.screen_succeed_money_send) {
    private val binding by viewBinding(ScreenSucceedMoneySendBinding::bind)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(SucceedMoneySendScreenDirections.actionSucceedMoneySendScreenToBasicScreen())
                }
            })

        btnBack.apply {
            setOnTouchListener { view, motionEvent ->
                val action = motionEvent.action

                if (action == MotionEvent.ACTION_MOVE) {
                    txtBack.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        )
                    )
                    imgBack.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
                if (action == MotionEvent.ACTION_UP) {
                    txtBack.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        )
                    )
                    imgBack.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    findNavController().navigate(SucceedMoneySendScreenDirections.actionSucceedMoneySendScreenToBasicScreen())
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    txtBack.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyBg
                        )
                    )
                    imgBack.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyBg
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
                true
            }
        }

        btnCheque.apply {
            setOnTouchListener { view, motionEvent ->
                val action = motionEvent.action

                if (action == MotionEvent.ACTION_MOVE) {
                    txtCheque.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        )
                    )
                    imgCheque.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
                if (action == MotionEvent.ACTION_UP) {
                    txtCheque.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        )
                    )
                    imgCheque.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    showToast("Cheque clicked")
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    txtCheque.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyBg
                        )
                    )
                    imgCheque.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyBg
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
                true
            }
        }

        btnSave.apply {
            setOnTouchListener { view, motionEvent ->
                val action = motionEvent.action

                if (action == MotionEvent.ACTION_MOVE) {
                    txtSave.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        )
                    )
                    imgSave.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
                if (action == MotionEvent.ACTION_UP) {
                    txtSave.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        )
                    )
                    imgSave.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyColor
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    showToast("Save clicked")
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    txtSave.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyBg
                        )
                    )
                    imgSave.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyBg
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
                true
            }
        }
    }
}