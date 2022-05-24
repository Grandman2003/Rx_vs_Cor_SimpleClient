package com.example.retrsample2.di

import com.example.retrsample2.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetServiceProviderModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder{
        @BindsInstance fun withURL(@URL URL: String): Builder
        fun build(): AppComponent
    }
}