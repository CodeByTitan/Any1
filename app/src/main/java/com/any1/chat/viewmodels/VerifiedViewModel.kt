package com.any1.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class VerifiedViewModel:ViewModel() {
    private val isVerified = MutableLiveData<Boolean>()
    val liveIsVerified : LiveData<Boolean> = isVerified
    private val auth = FirebaseAuth.getInstance()

    init {
        isVerified.value = auth.currentUser!!.isEmailVerified
    }

    fun checkEmailVerification(){
        auth.currentUser!!.reload()
        isVerified.postValue(auth.currentUser!!.isEmailVerified)
    }
//    fun setverification(input:Boolean){
//        input = isVerified
//    }
//    fun updateVerificationStatus(){
//        isVerified.postValue(miniRepository.checkEmailVerification())
//    }
}