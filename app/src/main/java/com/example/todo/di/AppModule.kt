package com.example.todo.di

import android.content.Context
import com.example.todo.datastore.UserPreferences
import com.example.todo.network.TodoAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserPreference(@ApplicationContext context : Context) = UserPreferences(context)

    @Singleton
    @Provides
    fun provideTodoApi() :TodoAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api-nodejs-todolist.herokuapp.com")
            .client(
                OkHttpClient.Builder().also {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    it.addInterceptor(logging)
                }.build()
            )
            .build()
            .create(TodoAPI::class.java)
    }
}