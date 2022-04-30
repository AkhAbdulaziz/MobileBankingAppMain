package uz.targetsoftwaredevelopment.mobilebankingapp.data.remote.card_req_res.request

data class IgnoreBalanceRequest(
    val userCardId: Int,
    val ignoreBalance: Boolean
)
