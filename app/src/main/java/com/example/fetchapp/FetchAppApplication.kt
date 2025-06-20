package com.example.fetchapp

import android.app.Application
import com.example.fetchapp.data.AppContainer
import com.example.fetchapp.data.DefaultAppContainer

class FetchAppApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}