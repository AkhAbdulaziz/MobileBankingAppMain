package uz.gita.mobilebankingapp.data.enums

import uz.gita.mobilebankingapp.R

enum class TransactionTypes {
    AIR, AUTO, ACCESSORIES, RENT, CONVERSION, CREDIT, SHOP, REST, TAXI, TRANSFER, TRANSPORT;

    fun getBgTintColor(): Int {
        return when (this) {
            AIR -> R.color.baseColor
            AUTO -> R.color.lightBaseColor
            ACCESSORIES -> R.color.brown
            RENT -> R.color.colorGreen
            CONVERSION -> R.color.yellow2
            CREDIT -> R.color.colorRed
            SHOP -> R.color.purple_200
            REST -> R.color.purple_500
            TAXI -> R.color.yellow
            TRANSFER -> R.color.baseColor
            else -> R.color.blue_grey
        }
    }
}

/*
    <string name="ru_transaction_air">авиа</string>
    <string name="ru_transaction_auto">авто</string>
    <string name="ru_transaction_accessories">аксессуары</string>
    <string name="ru_transaction_rent">аренда</string>
    <string name="ru_transaction_conversion">конверсия</string>
    <string name="ru_transaction_credit">кредит</string>
    <string name="ru_transaction_shop">магазин</string>
    <string name="ru_transaction_rest">отдых</string>
    <string name="ru_transaction_taxi">такси</string>
    <string name="ru_transaction_transport">транспорт</string>
*/