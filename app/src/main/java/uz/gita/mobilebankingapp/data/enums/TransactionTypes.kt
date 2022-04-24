package uz.gita.mobilebankingapp.data.enums

import uz.gita.mobilebankingapp.R

enum class TransactionTypes {
    AIR, AUTO, ACCESSORIES, RENT, CONVERSION, CREDIT, SHOP, REST, TAXI, TRANSFER, TRANSPORT;

    fun getBgTintColor(): Int {
        return when (this) {
            AIR -> R.color.transaction_air
            AUTO -> R.color.transaction_auto
            ACCESSORIES -> R.color.transaction_accessories
            RENT -> R.color.transaction_rent
            CONVERSION -> R.color.transaction_conversion
            CREDIT -> R.color.transaction_credit
            SHOP -> R.color.transaction_shop
            REST -> R.color.transaction_rest
            TAXI -> R.color.transaction_taxi
            TRANSFER -> R.color.transaction_transfer
            TRANSPORT -> R.color.transaction_transport
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