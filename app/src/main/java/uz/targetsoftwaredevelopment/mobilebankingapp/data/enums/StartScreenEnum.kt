package uz.targetsoftwaredevelopment.mobilebankingapp.data.enums

enum class StartScreenEnum {
    LOGIN, MAIN
}
fun String.getStartScreen() : StartScreenEnum {
    return if (this == StartScreenEnum.LOGIN.name) StartScreenEnum.LOGIN
    else StartScreenEnum.MAIN
}
