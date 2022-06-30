package com.any1.chat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.any1.chat.R

class learnmore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learnmore)

    }
    private fun slideleft(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val bndlAnimation = ActivityOptions.makeCustomAnimation(
//            applicationContext,
//        ).toBundle()
        startActivity(intent)
        overridePendingTransition(
            R.anim.slideleft2,
            R.anim.slideleft1
        )
    }
    override fun onBackPressed() {
        slideleft()
    }
}
