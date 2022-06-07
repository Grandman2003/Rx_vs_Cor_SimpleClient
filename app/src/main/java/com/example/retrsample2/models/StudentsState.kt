package com.example.retrsample2.models

enum class StudentsState {
    SHOWN{
        override fun getState() = "Students are shown"
         },
    ADDED{
        override fun getState(): String  = "Student successfully added"
    };
    abstract fun getState(): String
}