package uz.gita.mobilebankingapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.mobilebankingapp.data.remote.ApiClient
import uz.gita.mobilebankingapp.data.remote.api.AuthApi
import uz.gita.mobilebankingapp.data.remote.api.CardApi
import uz.gita.mobilebankingapp.data.remote.api.MoneyTransferApi
import uz.gita.mobilebankingapp.data.remote.api.ProfileApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getAuthApi(): AuthApi = ApiClient.retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun getCardApi(): CardApi = ApiClient.retrofit.create(CardApi::class.java)

    @Provides
    @Singleton
    fun getProfileApi(): ProfileApi = ApiClient.retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun getMoneyTransferApi(): MoneyTransferApi =
        ApiClient.retrofit.create(MoneyTransferApi::class.java)
}