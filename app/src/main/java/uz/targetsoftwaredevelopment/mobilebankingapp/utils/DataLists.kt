package uz.targetsoftwaredevelopment.mobilebankingapp.utils

import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.app.App
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.*
import uz.targetsoftwaredevelopment.mobilebankingapp.data.enums.TransactionTypes

val savedPaymentsList: ArrayList<SavedPaymentData> = arrayListOf(
    SavedPaymentData(
        false,
        R.drawable.logo_beeline,
        "Beeline",
        "My phone",
        "+998991002030",
        92000
    ),
    SavedPaymentData(
        false,
        R.drawable.logo_beeline,
        "Beeline",
        "Granny's phone",
        "+998919998877",
        50000
    ),
    SavedPaymentData(
        false,
        R.drawable.logo_atto,
        "ATTO",
        "My ATTO",
        "14 000 sum",
        14000
    ),
    SavedPaymentData(
        false,
        R.drawable.logo_mobiuz,
        "UMS",
        "brother",
        "+998901510051",
        22000
    ),
    SavedPaymentData(
        true,
        R.drawable.logo_mobiuz,
        "UMS",
        "brother",
        "+998901510051",
        30000
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
        App.instance.getString(R.string.en_payment_for_service_popular)
    ),
    PaymentForServicesData(
        R.drawable.ic_smartphone,
        App.instance.getString(R.string.en_payment_for_service_mobile_operators)
    ),
    PaymentForServicesData(
        R.drawable.ic_internet,
        App.instance.getString(R.string.en_payment_for_service_internet_providers)
    ),
    PaymentForServicesData(
        R.drawable.ic_house,
        App.instance.getString(R.string.en_payment_for_service_communal)
    ),
    PaymentForServicesData(
        R.drawable.ic_government,
        App.instance.getString(R.string.en_payment_for_service_government)
    ),
    PaymentForServicesData(
        R.drawable.ic_phone,
        App.instance.getString(R.string.en_payment_for_service_telephone)
    ),
    PaymentForServicesData(
        R.drawable.ic_tv,
        App.instance.getString(R.string.en_payment_for_service_tv)
    ),
    PaymentForServicesData(
        R.drawable.ic_online_services,
        App.instance.getString(R.string.en_payment_for_service_online_service)
    ),
    PaymentForServicesData(
        R.drawable.ic_poster,
        App.instance.getString(R.string.en_payment_for_service_poster)
    ),
    PaymentForServicesData(
        R.drawable.ic_bank_credit,
        App.instance.getString(R.string.en_payment_for_service_bank)
    ),
    PaymentForServicesData(
        R.drawable.ic_percentage,
        App.instance.getString(R.string.en_payment_for_service_credit)
    ),
    PaymentForServicesData(
        R.drawable.ic_bus,
        App.instance.getString(R.string.en_payment_for_service_transport)
    ),
    PaymentForServicesData(
        R.drawable.ic_heart,
        App.instance.getString(R.string.en_payment_for_service_charity)
    ),
    PaymentForServicesData(
        R.drawable.ic_hat_graduation,
        App.instance.getString(R.string.en_payment_for_service_education)
    ),
    PaymentForServicesData(
        R.drawable.ic_wallet,
        App.instance.getString(R.string.en_payment_for_service_electronic_wallet)
    ),
    PaymentForServicesData(
        R.drawable.ic_umbrella,
        App.instance.getString(R.string.en_payment_for_service_insurance)
    ),
    PaymentForServicesData(
        R.drawable.ic_game_controller,
        App.instance.getString(R.string.en_payment_for_service_game_social_media)
    ),
    PaymentForServicesData(
        R.drawable.ic_airplane,
        App.instance.getString(R.string.en_payment_for_service_airplane)
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

val servicesList: ArrayList<ServicesData> = arrayListOf(
    ServicesData(
        R.drawable.img_service1,
        App.instance.getString(R.string.en_services_title_payme_avia),
        App.instance.getString(R.string.en_services_description_payme_avia)
    ),
    ServicesData(
        R.drawable.img_service2,
        App.instance.getString(R.string.en_services_title_payment_for_checking_account),
        App.instance.getString(R.string.en_services_description_payment_for_checking_account)
    ),
    ServicesData(
        R.drawable.img_service3,
        App.instance.getString(R.string.en_services_title_iib_debt_checking),
        App.instance.getString(R.string.en_services_description_iib_debt_checking)
    ),
    ServicesData(
        R.drawable.img_service4,
        App.instance.getString(R.string.en_services_title_gold_crown),
        App.instance.getString(R.string.en_services_description_gold_crown)
    ),
    ServicesData(
        R.drawable.img_service5,
        App.instance.getString(R.string.en_services_title_payment_monitoring),
        App.instance.getString(R.string.en_services_description_payment_monitoring)
    ),
    ServicesData(
        R.drawable.img_service6,
        App.instance.getString(R.string.en_services_title_traffic_fine_notices),
        App.instance.getString(R.string.en_services_description_traffic_fine_notices)
    ),
    ServicesData(
        R.drawable.img_service7,
        App.instance.getString(R.string.en_services_title_auto_insurance),
        App.instance.getString(R.string.en_services_description_auto_insurance)
    )
)

val locationPaymentsList: ArrayList<LocationPaymentsData> = arrayListOf(
    LocationPaymentsData(
        null,
        null,
        false,
        R.drawable.logo_c_space,
        App.instance.getString(R.string.location_c_space_name),
        App.instance.getString(R.string.location_c_space_address),
        "150 m",
        "Products",
        true
    ),
    LocationPaymentsData(
        null,
        null,
        false,
        R.drawable.logo_candy_dance_studio,
        App.instance.getString(R.string.location_candy_dance_studio_name),
        App.instance.getString(R.string.location_candy_dance_studio_address),
        "150 m",
        null,
        true
    ),
    LocationPaymentsData(
        null,
        null,
        false,
        R.drawable.logo_rukkola,
        App.instance.getString(R.string.location_rukkola_name),
        App.instance.getString(R.string.location_rukkola_address),
        "150 m",
        null,
        false
    )
)

val recipientsList: ArrayList<RecipientData> = arrayListOf(
    RecipientData(
        "SP",
        "Steven Paul Jobs",
        R.color.colorRed
    ),
    RecipientData(
        "WB",
        "William Bradley Pitt",
        R.color.yellow
    ),
    RecipientData(
        "WH",
        "William Henry Gates",
        R.color.colorGreen
    ),
    RecipientData(
        "ME",
        "Mark Elliot Zuckerberg",
        R.color.orange
    ),
    RecipientData(
        "SM",
        "Sergey Mikhaylovich Brin",
        R.color.purple_500
    ),
    RecipientData(
        "LE",
        "Lawrence Edward Page",
        R.color.brown
    ),
    RecipientData(
        "PS",
        "Pichai Sundararajan",
        R.color.orange
    ),
    RecipientData(
        "",
        "Add Recipient",
        R.color.orange,
        true
    )
)

