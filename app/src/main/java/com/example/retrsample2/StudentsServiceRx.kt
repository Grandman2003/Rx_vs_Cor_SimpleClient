package com.example.retrsample2

import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface StudentsServiceRx {
    @get:GET("/names/players/list")
    val students: Maybe<ArrayList<Student?>?>

    @POST("/names/players/add")
    fun putStudent(@Body student: Student?): Maybe<Student?>
}