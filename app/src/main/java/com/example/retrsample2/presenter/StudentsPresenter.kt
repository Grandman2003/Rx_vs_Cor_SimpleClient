package com.example.retrsample2.presenter

import android.app.Application
import android.util.Log
import com.example.retrsample2.App
import com.example.retrsample2.di.DaggerAppComponent
import com.example.retrsample2.models.Student
import com.example.retrsample2.net.StudentsServiceRx
import com.example.retrsample2.views.ShowStudentsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Retrofit
import javax.inject.Inject

@InjectViewState
class StudentsPresenter(applicationContext: Application): MvpPresenter<ShowStudentsView>() {
    @Inject lateinit var retrofitRx: StudentsServiceRx
    val disposeBag = CompositeDisposable()
    init{
        (applicationContext as App).appComponent.inject(this)
    }
    fun putStudent(student: Student){
        val singlePut = retrofitRx.putStudent(student)
        val disposablePut = singlePut.subscribe({
            viewState.studentAdded()
            Log.d("SEND_AND_RETURN", "Student: $it")
        },{
            viewState.showError(it.localizedMessage)
            Log.e("MAINACTIVITY", it.localizedMessage?.toString() ?: "Nothing, I am sorry")
        })
        disposeBag.add(disposablePut)
    }

    fun showStudents(){
        val singleGet =  retrofitRx.students
        val disposeGet = singleGet
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({listStudent ->
                Log.d("SEND_AND_RETURN", "get list  with lenght " + listStudent?.size)
                var str = ""
                if (listStudent != null) {
                    for (s in listStudent) {
                        str += "Студент: $s\n"
                    }
                }
                viewState.showStudents(str)
            },{
                viewState.showError(it.localizedMessage)
                it.printStackTrace()
            })
        disposeBag.add(disposeGet)
    }

    override fun onDestroy() {
        disposeBag.clear()
        super.onDestroy()
    }
}