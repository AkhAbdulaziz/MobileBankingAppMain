package uz.targetsoftwaredevelopment.mobilebankingapp.data.entities

data class RecipientData(
    var abbreviationName: String,
    var fullName: String,
    var bgColor: Int,
    var isItLast: Boolean = false
)
