package com.sumup.countryapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.sumup.countryapp.api.CountryAppApi
import com.sumup.countryapp.repository.CountryRepository
import com.sumup.countryapp.repository.CountryRepositoryImpl
import com.sumup.countryapp.repository.FavouritesRepository
import com.sumup.countryapp.repository.FavouritesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.moshi.MoshiConverterFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favourites")


@Module
@InstallIn(SingletonComponent::class)
interface AppModule {


    @Binds
    @Singleton
    abstract fun provideCountryRepository(repo: CountryRepositoryImpl): CountryRepository

    @Binds
    @Singleton
    abstract fun provideFavouritesRepository(repo: FavouritesRepositoryImpl): FavouritesRepository

    companion object AppModule {

        @Provides
        @Singleton
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @Provides
        @Singleton
        fun provideAuthInterceptor(): Interceptor {
            return Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                if (BuildConfig.REST_COUNTRIES_API_TOKEN.isNotBlank()) {
                    requestBuilder.addHeader(
                        "Authorization",
                        "Bearer ${BuildConfig.REST_COUNTRIES_API_TOKEN}",
                    )
                }
                chain.proceed(requestBuilder.build())
            }
        }

        @Provides
        @Singleton
        fun provideHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authInterceptor: Interceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(httpClient: OkHttpClient): retrofit2.Retrofit {
            return retrofit2.Retrofit.Builder()
                .baseUrl("https://api.restcountries.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .build()
        }


        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }

        @Provides
        @Singleton
        fun provideCountryAppApi(retrofit: Retrofit): CountryAppApi{
            return retrofit.create(CountryAppApi::class.java)
        }


    }
}