package com.any1.chat.viewmodels

import androidx.lifecycle.ViewModel

class emailbhiewmodel:ViewModel() {
    var email = ""
    @JvmName("setEmail1")
    fun setEmail(input:String){
        email = input
    }
    @JvmName("getEmail1")
    fun getEmail(input:String){
        email = input
    }
}