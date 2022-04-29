package uz.gita.mobilebankingapp.data.entities

data class UserLocalData(
    var firstName: String = "First name",
    var lastName: String = "Last name",
    var nickname: String = "",
    var phoneNumber1: String = "+998",
    var phoneNumber2: String = "+998",
    var gender: String = "Does not set",
    var birthday: String = ""
)