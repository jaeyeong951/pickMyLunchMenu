package com.example.pickmylunchmenu.diConfig

import android.content.Context
import androidx.room.Room
import com.example.pickmylunchmenu.dao.HistoryDao
import com.example.pickmylunchmenu.db.HistoryDataBase
import com.example.pickmylunchmenu.repository.HistoryRepository
import com.example.pickmylunchmenu.repository.HistoryRepositoryImpl
import com.example.pickmylunchmenu.repository.RestaurantRepository
import com.example.pickmylunchmenu.repository.RestaurantRepositoryImpl
import com.example.pickmylunchmenu.service.RestaurantService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestaurantApiConfig {

    @Singleton
    @Provides
    fun provideRestaurantRepository(restaurantService: RestaurantService) : RestaurantRepository
        = RestaurantRepositoryImpl(restaurantService)

    @Singleton
    @Provides
    fun provideRestaurantService(retrofit: Retrofit): RestaurantService
        = retrofit.create(RestaurantService::class.java)

    @Singleton
    @Provides
    fun provideNewsApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("https://b1503903daf8.ngrok.io").client(okHttpClient)
//            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpClientBuilder = OkHttpClient().newBuilder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(logging).connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideHistoryDatabase(@ApplicationContext applicationContext: Context): HistoryDataBase
        = Room.databaseBuilder(applicationContext, HistoryDataBase::class.java, "historyDatabase.db").build()

    @Singleton
    @Provides
    fun provideHistoryDao(historyDataBase: HistoryDataBase): HistoryDao= historyDataBase.historyDao()

    @Singleton
    @Provides
    fun provideHistoryRepository(historyDao: HistoryDao): HistoryRepository = HistoryRepositoryImpl(historyDao)
}