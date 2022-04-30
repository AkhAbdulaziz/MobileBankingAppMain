package uz.targetsoftwaredevelopment.mobilebankingapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AppRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.AuthRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.CardRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.HistoryRepository
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.impl.AppRepositoryImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.impl.AuthRepositoryImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.impl.CardRepositoryImpl
import uz.targetsoftwaredevelopment.mobilebankingapp.domain.repository.impl.HistoryRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun getAppRepository(repository: AppRepositoryImpl): AppRepository

    @Binds
    @Singleton
    fun getAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun getCardRepository(repository: CardRepositoryImpl): CardRepository

    @Binds
    @Singleton
    fun getHistoryRepository(repository: HistoryRepositoryImpl): HistoryRepository
}
