package com.example.retrsample2.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import com.example.retrsample2.App
import com.example.retrsample2.databinding.ActivityMainBinding
import com.example.retrsample2.databinding.ActivityMoxyBinding
import com.example.retrsample2.di.DaggerAppComponent
import com.example.retrsample2.models.Student
import com.example.retrsample2.models.StudentsState
import com.example.retrsample2.net.StudentsServiceCor
import com.example.retrsample2.net.StudentsServiceRx
import com.example.retrsample2.presenter.StudentsPresenter
import com.example.retrsample2.views.ShowStudentsView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MoxyActivity: MvpAppCompatActivity(), ShowStudentsView {
    lateinit var binding: ActivityMoxyBinding
    @InjectPresenter(tag = ".StudentsPresenter") lateinit var studentsPresenter: StudentsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMoxyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RxJavaRealization()
    }

    fun RxJavaRealization(){
            binding.send.setOnClickListener {
                val n = binding.name.text.toString()
                val a: Int = binding.age.text.toString().toInt()
                studentsPresenter.putStudent(Student(n,a))
            }
            binding.get.setOnClickListener{studentsPresenter.showStudents()}
    }
    @ProvidePresenter(tag= ".StudentsPresenter")
    fun getCurrentPresenter(): StudentsPresenter{
        return StudentsPresenter(application)
    }

    override fun showStudents(string: String) {
        binding.list.text = string
        Toast.makeText(applicationContext,StudentsState.SHOWN.getState(),Toast.LENGTH_LONG).show()
    }
    override fun studentAdded() { Snackbar.make(binding.root,StudentsState.ADDED.getState(),Snackbar.LENGTH_LONG).show() }
    override fun showError(message: String) { Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show() }
}