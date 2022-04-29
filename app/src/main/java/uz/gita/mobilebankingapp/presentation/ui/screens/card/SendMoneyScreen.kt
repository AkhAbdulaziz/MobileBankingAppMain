package uz.gita.mobilebankingapp.presentation.ui.screens.card

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.data.remote.card_req_res.CardData
import uz.gita.mobilebankingapp.data.remote.card_req_res.request.MoneyRequest
import uz.gita.mobilebankingapp.databinding.ScreenSendMoneyBinding
import uz.gita.mobilebankingapp.presentation.ui.adapter.paymentPageAdapters.SenderCardsPagerAdapter
import uz.gita.mobilebankingapp.presentation.viewmodels.base.card.SendMoneyViewModel
import uz.gita.mobilebankingapp.presentation.viewmodels.impl.card.SendMoneyViewModelImpl
import uz.gita.mobilebankingapp.utils.dpToPx
import uz.gita.mobilebankingapp.utils.gone
import uz.gita.mobilebankingapp.utils.scope
import uz.gita.mobilebankingapp.utils.visible

@AndroidEntryPoint
class SendMoneyScreen : Fragment(R.layout.screen_send_money) {
    private val binding by viewBinding(ScreenSendMoneyBinding::bind)
    private val viewModel: SendMoneyViewModel by viewModels<SendMoneyViewModelImpl>()
    private val args: SendMoneyScreenArgs by navArgs()
    private lateinit var pagerAdapter: SenderCardsPagerAdapter
    private lateinit var myCardsList: List<CardData>
    private var fullAmount: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllCardList()

        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        cardNumberText.text =
            "${args.cardNumber.substring(0, 6)}******${args.cardNumber.substring(12)}"
        receiverNameText.text = "John Smith"
        moneyAmountText.text = "${args.amount} sum"
        feeText.text = "${args.amount / 100} sum"
        fullAmountText.text = "${args.amount + args.amount / 100} sum"

        /*  val mainCardData: CardData? = viewModel.getMyMainCardData()
        if (mainCardData != null) {

*//*
            // TODO :Fee ni olishda ham backend da xatolik bor, api ni Body qabul qilmaydi deyapti.

//             viewModel.getFee(
//                MoneyRequest(
//                    mainCardData.id!!,
//                    args.cardNumber,
//                    args.amount
//                )
//            )

            // TODO: Card bg ham berib bo'lmayapti

//             cardBg.setBackgroundColor(
//                 ContextCompat.getColor(
//                     App.instance,
//                     mainCardData.color
//                 )
//             )
*//*

            if (mainCardData.color != null) {
                cardBg.setImageResource(cardBgImagesList[mainCardData.color])
            } else {
                cardBg.setImageResource(cardBgImagesList[Random().nextInt(16)])
            }
            senderName.text = mainCardData.cardName
            senderBalance.text = "Balance: ${mainCardData.balance} so'm"
            senderCardNumber.text = "**** ${mainCardData.pan!!.substring(12)}"

            if (mainCardData.balance!! < args.amount) {
                sendBtn.isEnabled = false
                showErrorNoMoney()
            } else {
                hideErrors()
                sendBtn.isEnabled = true
            }
        } else {
            sendBtn.isEnabled = false
            showErrorNoCard()

        }
*/

        txtAddCardOrOpenAWallet.setOnClickListener {
            findNavController().navigate(SendMoneyScreenDirections.actionSendMoneyScreenToAddCardScreen())
        }

        sendBtn.setOnClickListener {
            findNavController().navigate(
                SendMoneyScreenDirections.actionSendMoneyScreenToWaitingMoneySendScreen(
                    MoneyRequest(
                        myCardsList[viewpagerSenderCards.currentItem].id!!,
                        args.cardNumber,
                        args.amount
                    )
                )
            )
        }

        viewModel.enableSendMoneyButton.observe(viewLifecycleOwner, enableSendMoneyObserver)
        viewModel.cardsListLiveData.observe(viewLifecycleOwner, cardsListObserver)
//        viewModel.feeLiveData.observe(viewLifecycleOwner, feeObserver)
    }


    private val enableSendMoneyObserver = Observer<Unit> {
        binding.sendBtn.isEnabled = true
    }

    private val cardsListObserver = Observer<List<CardData>> { allMyCardsList ->
        myCardsList = allMyCardsList
        binding.scope {
            if (allMyCardsList.isEmpty()) {
                sendBtn.isEnabled = false
                showErrorNoCard()
            } else {
                txtAddCardOrOpenAWallet.gone()
                pagerAdapter =
                    SenderCardsPagerAdapter(
                        args.cardNumber,
                        args.amount,
                        allMyCardsList,
                        childFragmentManager,
                        lifecycle
                    )
                viewpagerSenderCards.apply {
                    adapter = pagerAdapter
                    clipToPadding = false   // allow full width shown with padding
                    clipChildren = false    // allow left/right item is not clipped
                    offscreenPageLimit = 2  // make sure left/right item is rendered
                }
                // increase this offset to show more of left/right
                val offsetPx = 16.dpToPx(resources.displayMetrics)
                viewpagerSenderCards.setPadding(offsetPx, 0, offsetPx, 0)

                // increase this offset to increase distance between 2 items
                val pageMarginPx = 2.dpToPx(resources.displayMetrics)
                val marginTransformer = MarginPageTransformer(pageMarginPx)
                viewpagerSenderCards.setPageTransformer(marginTransformer)
                TabLayoutMediator(senderCardsTabLayout, viewpagerSenderCards) { tab, pos ->

                }.attach()

                viewpagerSenderCards.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        val cardData: CardData = myCardsList[viewpagerSenderCards.currentItem]
                        sendBtn.isEnabled =
                            cardData.pan != args.cardNumber && cardData.balance!! >= args.amount
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    }

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)

                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                    }
                })

                pagerAdapter.setDisableSendButtonListener {
                    sendBtn.isEnabled = false
                }
                pagerAdapter.setEnableSendButtonListener {
                    sendBtn.isEnabled = true
                }
            }
        }
    }

//    private val feeObserver = Observer<String> {
//        binding.feeText.text = "$it so'm"
//        binding.fullAmountText.text = "${fullAmount + it.toLong()} so'm"
//    }

    private fun showErrorNoCard() = binding.scope {
        txtAddCardOrOpenAWallet.visible()
    }

    /* override fun onDestroyView() = binding.scope {
         super.onDestroyView()
         viewpagerSenderCards.removeOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->  }
     }*/
}
