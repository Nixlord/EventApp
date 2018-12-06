package com.example.overlord.eventapp

import android.util.Log

/**
 * All examples are true to my knowledge.
 * My knowledge is limited by the amount of kotlin I have read.
 * So please do not take this as the "only way things can be done".
 *
 * Every kotlin class inherits from "Any"
 * Unit is the type if you do not return anything.
 */

//No need to specify return type. Return type assumed void.
fun demoFunction(name : String, rollno : Int) {
    Log.d(name, "" + rollno)
}

/*
If you want to return something other than void you need to specify return type.
If you want return type to be inferred, you can use the next variant.
 */
fun squareLong(n : Int) : Int {
    return n * n
}

//Compiler will infer return type if you give expressions like this
fun squareNew(n : Int) = n * n


//This is immutable.
data class Person(val name : String, val age: Int? = null)

//Main Function has this signature
fun main(args : Array<String>) {
    for (arg in args) {
        Log.d("TAG", arg)
    }
}

fun personFunction() {
    val persons = listOf(
        Person("Shibasis", 22),
        Person("Diksha", 22),
        Person("Desktop")
    )

    val youngest = persons.minBy { it.age ?: 0 }
}