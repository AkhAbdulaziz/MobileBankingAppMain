package uz.gita.mobilebankingapp.data.entities

data class UserLocalData(
    var firstName: String = "",
    var lastName: String = "",
    var nickname: String = "",
    var phoneNumber1: String = "+998",
    var phoneNumber2: String = "+998",
    var gender: String = "Male",
    var birthday: String = "23.06.2002"
)