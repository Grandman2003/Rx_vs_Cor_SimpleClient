package com.example.retrsample2

data class Student (
    var name: String? = null,
    var lastName: String? = null,
    var firstName: String? = null,
    var age: Int = 0,
    var course: Int = 0,
    var isPass: Boolean = false)
{
    constructor(nameA: String?, ageA: Int) :this(name=nameA,age=ageA)

    override fun toString(): String {
        return name + " (" + age + "лет)"
    }
}

