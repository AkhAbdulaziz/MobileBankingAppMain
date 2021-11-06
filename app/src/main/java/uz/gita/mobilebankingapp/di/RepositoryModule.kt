package uz.gita.mobilebankingapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.mobilebankingapp.domain.repository.AppRepository
import uz.gita.mobilebankingapp.domain.repository.AuthRepository
import uz.gita.mobilebankingapp.domain.repository.CardRepository
import uz.gita.mobilebankingapp.domain.repository.impl.AppRepositoryImpl
import uz.gita.mobilebankingapp.domain.repository.impl.AuthRepositoryImpl
import uz.gita.mobilebankingapp.domain.repository.impl.CardRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun getAppRepository(repository: AppRepositoryImpl): AppRepository

    @Binds
    @Singleton
    abstract fun getAuthRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun getCardRepository(repository: CardRepositoryImpl): CardRepository
}
