package com.example.retrsample2

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.retrsample2.databinding.ActivityMainBinding
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.schedulers.RxThreadFactory
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MainActivity: AppCompatActivity() {
    // !!!! Установите свой адрес сервера !!!!
    lateinit var binding: ActivityMainBinding
    val URL = "http://192.168.1.72:8080/"
    var student: Student? = null
    val disposeBag = CompositeDisposable()
    val retrofitRx by lazy { Retrofit.Builder()
            .baseUrl(URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val retrofitCor by lazy { Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
    val serviceRx by lazy {
        retrofitRx.create(StudentsServiceRx::class.java)
    }
    val serviceCor by lazy {
        retrofitCor.create(StudentsServiceCor::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
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