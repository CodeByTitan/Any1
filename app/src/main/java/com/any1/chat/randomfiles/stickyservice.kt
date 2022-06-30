package com.any1.chat.randomfiles

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class StickyService : Service() {
    var handler: Handler? = null

    fun StickyService(){
        handler = Handler()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        showToast("service started", applicationContext)
        return null
    }

    fun showToast(message: String?, context: Context?) {
        handler!!.post(CustomToast(message,context))
    }

    class CustomToast(message: String?, context: Context?) : Runnable{
        var mText: String? = null
        var mContext: Context? = null

        fun CustomToast(text: String?, context: Context?) {
            mText = text
            mContext = context
        }
        override fun run() {
            Toast.makeText(mContext, mText, Toast.LENGTH_LONG).show();
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences =
            applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "")
        if (username != null) {
            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            firestore.collection("usernames").document(username).get()
                .addOnSuccessListener {
                    firestore.collection("usernames").document(username).delete().addOnCompleteListener {
                            task -> if (task.isSuccessful) {
                        Toast.makeText(this, "username deleted successfully", Toast.LENGTH_SHORT).show()
                        Log.d("appclosed", "username deleted succesfully")
                    }
                        val user = auth.currentUser
                        if(user!=null){
                            Toast.makeText(this, "user deleted", Toast.LENGTH_SHORT).show()
                            user.delete()
                        }
                    }.addOnFailureListener {
                        Log.d("appclosed", "username deletion unsucessful")
                    }
                }
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val sharedPreferences =
            applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "")
        if (username != null) {
            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            firestore.collection("usernames").document(username).get()
                .addOnSuccessListener {
                    firestore.collection("usernames").document(username).delete().addOnCompleteListener {
                        task -> if (task.isSuccessful) {
                        Toast.makeText(this, "username deleted successfully", Toast.LENGTH_SHORT).show()
                        Log.d("appclosed", "username deleted succesfully")
                    }
                        val user = auth.currentUser
                        if(user!=null){
                            user.delete()
                            Toast.makeText(this, "user deleted", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Log.d("appclosed", "username deletion unsucessful")
                    }
                }
        }
    }
}
