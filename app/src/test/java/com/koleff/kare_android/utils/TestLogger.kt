package com.koleff.kare_android.utils

class TestLogger(private val isLogging: Boolean = true) {

    fun i(tag: String, message: String){
        if(isLogging) println("$tag : $message")
    }

    fun e(tag: String, message: String, throwable: Throwable?) {
        if(isLogging) println("$tag : $message")
    }
}