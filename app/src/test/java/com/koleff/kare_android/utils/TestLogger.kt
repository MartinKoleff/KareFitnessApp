package com.koleff.kare_android.utils

class TestLogger {

    fun i(tag: String, message: String){
        println("$tag : $message")
    }

    fun e(tag: String, message: String, throwable: Throwable?) {
        println("$tag : $message")
    }
}