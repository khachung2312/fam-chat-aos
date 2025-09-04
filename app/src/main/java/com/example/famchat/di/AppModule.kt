package com.example.famchat.di

import com.example.famchat.database.ManagerDatabase
import com.example.famchat.database.repository.ManagerRepositoryImpl
import com.example.famchat.viewmodel.globall.GlobalViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { ManagerDatabase.getInstance(androidApplication()) }
    single { ManagerRepositoryImpl(get()) as ManagerRepositoryImpl }
    single { GlobalViewModel(androidApplication()) }
}