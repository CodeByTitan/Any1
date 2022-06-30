package com.any1.chat.viewmodels

import androidx.lifecycle.ViewModel

class AddAccountViewModel : ViewModel() {
    var add = false
    fun add(){
        add = true
    }
    fun dontadd(){
        add = false
    }
}