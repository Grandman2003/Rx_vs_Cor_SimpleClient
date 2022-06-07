package com.example.retrsample2.di

import com.example.retrsample2.presenter.StudentsPresenter
import com.example.retrsample2.ui.MainActivity
import com.example.retrsample2.ui.MoxyActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetServiceProviderModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(presenter: StudentsPresenter)

    @Component.Builder
    interface Builder{
        @BindsInstance fun withURL(@URL URL: String): Builder
        fun build(): AppComponent
    }
}