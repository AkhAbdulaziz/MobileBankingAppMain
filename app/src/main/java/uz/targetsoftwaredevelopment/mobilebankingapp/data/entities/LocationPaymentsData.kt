package uz.targetsoftwaredevelopment.mobilebankingapp.data.entities

import java.io.Serializable

data class LocationPaymentsData(
    var imgAlertError: Int? = null,
    var descriptionError: String? = null,
    var isErrorEnableTextVisible: Boolean = false,
    var imgLocation: Int? = null,
    var nameLocation: String? = null,
    var addressLocation: String? = null,
    var distanceLocation: String? = null,
    var tag: String? = null,
    val isUnderlineVisible: Boolean = true
) : Serializable
