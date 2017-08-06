package com.martin.plain

import com.google.gson.Gson


fun main(args: Array<String>)
{
    val dataJson = "{ \"field\" : \"kiskacsa\"}"
    val myData = Gson().fromJson(dataJson, MyData::class.java)
    println(myData hello "Martin")

    val someThing = (myData?.field?.length?.takeIf { it isBiggerThan 5 }
            ?.let { "$it is just enough" } ?: "too few"). toUpperCase() .also { println("side effect") }

    println(someThing)
}

data class MyData(val field: String?)
{
    infix fun hello(name : String) = println("that's a hello from the body to $name")
}

infix fun Int.isBiggerThan(other: Int) = this > other