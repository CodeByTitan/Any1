package com.any1.chat.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.any1.chat.R
import com.any1.chat.adapters.ChatFragmentPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var bottomnav : BottomNavigationView
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences2 = getSharedPreferences(packageName+"theme", MODE_PRIVATE)
        if(sharedPreferences2.getString("theme","")=="dark"){
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES;
        }else{
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewpager = findViewById<ViewPager2>(R.id.viewpager)
        val chatFragmentPagerAdapter = ChatFragmentPagerAdapter(this)
        viewpager.adapter = chatFragmentPagerAdapter
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_background)
    }


    override fun onNewIntent(intent: Intent?) {
        val extras = intent!!.extras
        if(extras !=null){
            val theme = extras.getString("theme")
            if(theme == "dark"){
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES;
            }else{
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO;
            }
        }
        super.onNewIntent(intent)
    }
    fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager
        val fragments: List<Fragment> = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible) return fragment
            }
        }
        return null
    }

    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("gotoprofilefragment")) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStackImmediate("MESSAGE_TAG", 0)
            }
        }
    }

}