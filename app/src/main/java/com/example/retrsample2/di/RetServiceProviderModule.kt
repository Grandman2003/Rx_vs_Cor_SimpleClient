package com.example.retrsample2.di;

import com.example.retrsample2.net.StudentsServiceCor
import com.example.retrsample2.net.StudentsServiceRx
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [RetrofitProviderModule::class])
class RetServiceProviderModule {
    @Inject
    @Provides fun getCorService( @CorProvider retrofit: Retrofit): StudentsServiceCor = retrofit.create(StudentsServiceCor::class.java)

    @Inject
    @Provides fun getRxService( @RxProvider retrofit: Retrofit): StudentsServiceRx = retrofit.create(StudentsServiceRx::class.java)
}
