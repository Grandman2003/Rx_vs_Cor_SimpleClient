package com.example.retrsample2.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.retrsample2.App
import com.example.retrsample2.net.StudentsServiceCor
import com.example.retrsample2.net.StudentsServiceRx
import com.example.retrsample2.databinding.ActivityMainBinding
import com.example.retrsample2.models.Student
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

class MainActivity: AppCompatActivity() {
    // !!!! Установите свой адрес сервера !!!!
    lateinit var binding: ActivityMainBinding
    var student: Student? = null
    val disposeBag = CompositeDisposable()
    @Inject lateinit var serviceRx: StudentsServiceRx
    @Inject lateinit var serviceCor: StudentsServiceCor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        (applicationContext as App).appComponent.inject(this)
        setContentView(binding.root)
        RxJavaRealization()
        //KotlinCoroutinesRealization()
    }

    fun RxJavaRealization(){
        binding.send.setOnClickListener {
            val n = binding.name.text.toString()
            val a: Int = binding.age.text.toString().toInt()
            student = Student(n, a)
            val singlePut = serviceRx.putStudent(student)
            val disposablePut = singlePut.subscribe({
                Log.d("SEND_AND_RETURN", "Student: $it")
            },{
                Log.e("MAINACTIVITY", it.localizedMessage?.toString() ?: "Nothing, I am sorry")
            })
            disposeBag.add(disposablePut)}

        binding.get.setOnClickListener {
            val singleGet =  serviceRx.students
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
                    binding.list.text = str
                },{
                    it.printStackTrace()
                })
            disposeBag.add(disposeGet)}
    }

    fun KotlinCoroutinesRealization(){
        var listStudent: List<Student> = emptyList()
        binding.send.setOnClickListener {
            val n = binding.name.text.toString()
            val a: Int = binding.age.text.toString().toInt()
            student = Student(n, a)
            lifecycleScope.launch(Dispatchers.IO) {
                try{
                    val call = serviceCor.putStudent(student)
                    val userResponse = call.execute()
                    val s = userResponse.body()
                    Log.d("SEND_AND_RETURN", "Student: $s")}
                catch (e: Exception){
                    Log.e("MAINACTIVITY", e.localizedMessage.toString())
                }
            }
        }
        binding.get.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
            val call: Call<ArrayList<Student?>?> = serviceCor.students
            try {
                val userResponse = call.execute()
                listStudent = userResponse.body() as List<Student>
                Log.d("SEND_AND_RETURN", "get list  with lenght " + listStudent.size)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            launch(Dispatchers.Main) {
                var str = ""
                for (s in listStudent) {
                    str += "Студент: $s\n"
                }
                binding.list.text = str
            }
        }
        }
    }

    override fun onDestroy() {
        disposeBag.clear()
        super.onDestroy()
    }

    }