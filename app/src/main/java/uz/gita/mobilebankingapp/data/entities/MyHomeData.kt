package uz.gita.mobilebankingapp.data.entities

import java.io.Serializable

data class MyHomeData(
    var isLast: Boolean = true,
    var name: String = "Add home",
    var isNameVisible: Boolean = true
) : Serializable
