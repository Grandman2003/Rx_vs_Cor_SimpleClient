package com.example.retrsample2

import android.app.Application
import com.example.retrsample2.di.AppComponent
import com.example.retrsample2.di.DaggerAppComponent

class App: Application() {
    val URL = "http://192.168.1.72:8080/"
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().withURL(URL).build()
    }
}