package com.harry.example.assignment

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harry.example.assignment.database.UserDatabase
import com.harry.example.assignment.database.entities.User
import com.harry.example.assignment.network.ApiClient
import com.harry.example.assignment.networkchecker.NetworkConnection
import com.harry.example.assignment.repository.AppRepository
import com.harry.example.assignment.utility.DATABASE_NAME
import com.harry.example.assignment.utility.Utility
import com.harry.example.assignment.viewmodels.GetImageViewModel
import com.harry.example.assignment.viewmodels.UserViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val appModule = module {
    single<NetworkConnection> { NetworkConnection(androidContext()) }
    single<Retrofit> { getRetrofit() }
    factory<ApiClient> { getApiClient(get()) }
    single<Utility> { Utility() }
    single<UserDatabase> { getUserDatabase(androidContext()) }
    single<AppRepository> { AppRepository(get(), get()) }
    viewModel<UserViewModel> {
        UserViewModel(androidApplication(), get())
    }
    viewModel<GetImageViewModel> { GetImageViewModel(androidApplication(), get()) }
}

private fun getUserDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(context, UserDatabase::class.java, DATABASE_NAME).build()
}

private fun getRetrofit(): Retrofit {
    return Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()
}

private fun getApiClient(retrofit: Retrofit): ApiClient {
    return retrofit.create(ApiClient::class.java)
}
