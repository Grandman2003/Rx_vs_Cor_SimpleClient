package com.example.retrsample2.net

import com.example.retrsample2.di.CorProvider
import com.example.retrsample2.models.Student
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StudentsServiceCor {
    @get:GET("/names/players/list")
    val students: Call<ArrayList<Student?>?>

    @POST("/names/players/add")
    fun putStudent(@Body student: Student?): Call<Student?>
}