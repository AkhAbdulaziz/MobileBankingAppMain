package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.viewmodels.base.auth

interface EnableFaceIDViewModel {
    fun changeFaceIDPermission(permission : Boolean)

    fun changeConfirmationByFingerPrintPermission(permission : Boolean)
}