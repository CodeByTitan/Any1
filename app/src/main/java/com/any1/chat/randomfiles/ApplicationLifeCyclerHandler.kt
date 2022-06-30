package com.any1.chat.randomfiles

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ApplicationLifeCyclerHandler : Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private val TAG = "AppLifeCycleShareTime"
    private var isInBackground = false
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        Log.d( TAG  , "onActivityCreated");
    }

    override fun onActivityStarted(p0: Activity) {
        Log.d( TAG  , "onActivityStarted");
    }

    override fun onActivityResumed(p0: Activity) {
        Log.d( TAG  , "onActivityResumed");
    }

    override fun onActivityPaused(p0: Activity) {
        Log.d( TAG  , "onActivityPaused");
    }

    override fun onActivityStopped(p0: Activity) {
        Log.d( TAG  , "onActivityStopped");
//        val sharedPreferences =
//            p0.getSharedPreferences("user", Service.MODE_PRIVATE)
//        val username = sharedPreferences?.getString("username", "")
//        if (username != null) {
//            val firestore = FirebaseFirestore.getInstance()
//            val auth = FirebaseAuth.getInstance()
//            firestore.collection("usernames").document(username).get()
//                .addOnSuccessListener {
//                    firestore.collection("usernames").document(username).delete().addOnSuccessListener {
//                        Log.d("appclosed","username deleted succesfully")
//                    }.addOnFailureListener{
//                        Log.d("appclosed","username deletion unsucessful")
//                    }
//                    val user = auth.currentUser
//                    if(user!=null){
//                        user.delete()
//                    }
//                }
//        }
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        Log.d( TAG  , "onActivitySaveInstanceState");
    }

    override fun onActivityDestroyed(p0: Activity) {
        Log.d( TAG  , "onActivityDestroyed");
        val sharedPreferences =
            p0.getSharedPreferences("user", Service.MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "")
        if (username != null) {
            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            firestore.collection("usernames").document(username).get()
                .addOnSuccessListener {
                    firestore.collection("usernames").document(username).delete()
                        .addOnSuccessListener {
                            Log.d("appclosed", "username deleted succesfully")
                        }.addOnFailureListener {
                        Log.d("appclosed", "username deletion unsucessful")
                    }
                    val user = auth.currentUser
                    if (user != null) {
                        user.delete()
                    }
                }
        }
    }

    override fun onConfigurationChanged(p0: Configuration) {
        Log.d( TAG , "onConfigurationChanged");
    }

    override fun onLowMemory() {
        Log.d( TAG , "onLowMemory");
    }

    override fun onTrimMemory(i: Int) {
        Log.d( TAG  , "onTrimMemory");
        if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Log.d(TAG, "app went to background");
            isInBackground = true
        }
    }

}