package uz.gita.mobilebankingapp.utils

import uz.gita.mobilebankingapp.R
import uz.gita.mobilebankingapp.app.App
import uz.gita.mobilebankingapp.data.entities.*
import uz.gita.mobilebankingapp.data.enums.TransactionTypes

val savedPaymentsList: ArrayList<SavedPaymentData> = arrayListOf(
    SavedPaymentData(
        false,
        R.drawable.logo_beeline,
        "My phone",
        "+998991002030"
    ),
    SavedPaymentData(
        false,
        R.drawable.logo_beeline,
        "Granny's phone",
        "+998919998877"
    ),
    SavedPaymentData(
        false,
        R.drawable.logo_atto,
        "My ATTO",
        "14 000 sum"
    ),
    SavedPaymentData(
        false,
        R.drawable.logo_mobiuz,
        "brother",
        "+998901510051"
    ),
    SavedPaymentData(
        true,
        R.drawable.logo_mobiuz,
        "brother",
        "+998901510051"
    )
)

val adsList: ArrayList<AdData> = arrayListOf(
    AdData(
        R.drawable.ad1,
        App.instance.getString(R.string.ru_ad1_title),
        App.instance.getString(R.string.ru_ad1_description),
    ),
    AdData(
        R.drawable.ad2,
        App.instance.getString(R.string.ru_ad2_title),
        App.instance.getString(R.string.ru_ad2_description),
    ),
    AdData(
        R.drawable.ad3,
        App.instance.getString(R.string.ru_ad3_title),
        App.instance.getString(R.string.ru_ad3_description),
    ),
    AdData(
        R.drawable.ad4,
        App.instance.getString(R.string.ru_ad4_title),
        App.instance.getString(R.string.ru_ad4_description),
    ),
    AdData(
        R.drawable.ad5,
        App.instance.getString(R.string.ru_ad5_title),
        App.instance.getString(R.string.ru_ad5_description),
    ),
    AdData(
        R.drawable.ad6,
        App.instance.getString(R.string.ru_ad6_title),
        App.instance.getString(R.string.ru_ad6_description),
    ),
    AdData(
        R.drawable.ad1,
        App.instance.getString(R.string.ru_ad1_title),
        App.instance.getString(R.string.ru_ad1_description),
    ),
    AdData(
        R.drawable.ad2,
        App.instance.getString(R.string.ru_ad2_title),
        App.instance.getString(R.string.ru_ad2_description),
    ),
    AdData(
        R.drawable.ad3,
        App.instance.getString(R.string.ru_ad3_title),
        App.instance.getString(R.string.ru_ad3_description),
    ),
    AdData(
        R.drawable.ad4,
        App.instance.getString(R.string.ru_ad4_title),
        App.instance.getString(R.string.ru_ad4_description),
    ),
    AdData(
        R.drawable.ad5,
        App.instance.getString(R.string.ru_ad5_title),
        App.instance.getString(R.string.ru_ad5_description),
    ),
    AdData(
        R.drawable.ad6,
        App.instance.getString(R.string.ru_ad6_title),
        App.instance.getString(R.string.ru_ad6_description),
    ),
    AdData(
        R.drawable.ad1,
        App.instance.getString(R.string.ru_ad1_title),
        App.instance.getString(R.string.ru_ad1_description),
    ),
    AdData(
        R.drawable.ad2,
        App.instance.getString(R.string.ru_ad2_title),
        App.instance.getString(R.string.ru_ad2_description),
    ),
    AdData(
        R.drawable.ad3,
        App.instance.getString(R.string.ru_ad3_title),
        App.instance.getString(R.string.ru_ad3_description),
    ),
    AdData(
        R.drawable.ad4,
        App.instance.getString(R.string.ru_ad4_title),
        App.instance.getString(R.string.ru_ad4_description),
    ),
    AdData(
        R.drawable.ad5,
        App.instance.getString(R.string.ru_ad5_title),
        App.instance.getString(R.string.ru_ad5_description),
    ),
    AdData(
        R.drawable.ad6,
        App.instance.getString(R.string.ru_ad6_title),
        App.instance.getString(R.string.ru_ad6_description),
    )
)

val paymentForServicesList: ArrayList<PaymentForServicesData> = arrayListOf(
    PaymentForServicesData(
        R.drawable.ic_popular,
        App.instance.getString(R.string.ru_payment_for_service_popular)
    ),
    PaymentForServicesData(
        R.drawable.ic_smartphone,
        App.instance.getString(R.string.ru_payment_for_service_mobile_operators)
    ),
    PaymentForServicesData(
        R.drawable.ic_internet,
        App.instance.getString(R.string.ru_payment_for_service_internet_providers)
    ),
    PaymentForServicesData(
        R.drawable.ic_house,
        App.instance.getString(R.string.ru_payment_for_service_communal)
    ),
    PaymentForServicesData(
        R.drawable.ic_government,
        App.instance.getString(R.string.ru_payment_for_service_government)
    ),
    PaymentForServicesData(
        R.drawable.ic_phone,
        App.instance.getString(R.string.ru_payment_for_service_telephone)
    ),
    PaymentForServicesData(
        R.drawable.ic_tv,
        App.instance.getString(R.string.ru_payment_for_service_tv)
    ),
    PaymentForServicesData(
        R.drawable.ic_online_services,
        App.instance.getString(R.string.ru_payment_for_service_online_service)
    ),
    PaymentForServicesData(
        R.drawable.ic_poster,
        App.instance.getString(R.string.ru_payment_for_service_poster)
    ),
    PaymentForServicesData(
        R.drawable.ic_bank_credit,
        App.instance.getString(R.string.ru_payment_for_service_bank)
    ),
    PaymentForServicesData(
        R.drawable.ic_percentage,
        App.instance.getString(R.string.ru_payment_for_service_credit)
    ),
    PaymentForServicesData(
        R.drawable.ic_bus,
        App.instance.getString(R.string.ru_payment_for_service_transport)
    ),
    PaymentForServicesData(
        R.drawable.ic_heart,
        App.instance.getString(R.string.ru_payment_for_service_charity)
    ),
    PaymentForServicesData(
        R.drawable.ic_hat_graduation,
        App.instance.getString(R.string.ru_payment_for_service_education)
    ),
    PaymentForServicesData(
        R.drawable.ic_wallet,
        App.instance.getString(R.string.ru_payment_for_service_electronic_wallet)
    ),
    PaymentForServicesData(
        R.drawable.ic_umbrella,
        App.instance.getString(R.string.ru_payment_for_service_insurance)
    ),
    PaymentForServicesData(
        R.drawable.ic_game_controller,
        App.instance.getString(R.string.ru_payment_for_service_game_social_media)
    ),
    PaymentForServicesData(
        R.drawable.ic_airplane,
        App.instance.getString(R.string.ru_payment_for_service_airplane)
    )
)

val myHomesList: ArrayList<MyHomeData> = arrayListOf(
    MyHomeData(
        false,
        "My home1",
        true
    ),
    MyHomeData(
        false,
        "My home2",
        false
    ),
    MyHomeData(
        true,
        "My home2",
        false
    )
)

val recentTransactionsList: ArrayList<RecentTransactionData> = arrayListOf(
    RecentTransactionData(
        null,
        "myinfin online\nconvert",
        "21 February",
        "00:39",
        "-5 430",
        TransactionTypes.CONVERSION
    ),
    RecentTransactionData(
        null,
        "myinfin online\nconvert",
        "21 February",
        "00:34",
        "-124 564.2",
        TransactionTypes.CONVERSION
    ),
    RecentTransactionData(
        R.drawable.infinbank_logo,
        "",
        "21 February",
        "00:33",
        "-70 700",
        TransactionTypes.TRANSFER
    )
)
