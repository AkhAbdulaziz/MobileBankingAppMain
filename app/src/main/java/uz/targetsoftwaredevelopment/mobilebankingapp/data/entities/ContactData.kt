package uz.targetsoftwaredevelopment.mobilebankingapp.data.entities

import java.io.Serializable

data class ContactData(
    var fullName : String,
    var phone : String
) : Serializable
