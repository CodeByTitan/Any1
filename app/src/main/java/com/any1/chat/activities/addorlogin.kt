package com.any1.chat.activities

import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.any1.chat.R
import com.google.firebase.auth.FirebaseAuth


class addorlogin : AppCompatActivity() {
    // ...
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
    private lateinit var auth: FirebaseAuth
    private lateinit var videoView:VideoView
    private var stopPosition:Int = 0
    private lateinit var shp:SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
//    var resultLauncher = registerForActivityResult(
//        StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK) {
//            val intent = result.data
//            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account =
//                    (task as Task<*>).getResult(
//                        ApiException::class.java
//                    ) as GoogleSignInAccount
//                account.idToken?.let { firebaseAuthWithGoogle(it) }
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Toast.makeText(this, "Cannot get result", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//
//    lateinit var mGoogleSignInClient:GoogleSignInClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addorlogin)
        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        val addme = findViewById<Button>(R.id.addme)
        val login = findViewById<Button>(R.id.login)
        shp = applicationContext.getSharedPreferences(packageName+"login", MODE_PRIVATE)
        editor = shp.edit()
        auth = FirebaseAuth.getInstance()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        videoView = findViewById<VideoView>(R.id.videoview)
        val uri = Uri.parse("android.resource://com.any1.chat/"+ R.raw.loginvideo1)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
        videoView.setOnPreparedListener(OnPreparedListener { mp -> mp.isLooping = true })
        videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE);

//        createRequest()
        addme.setOnClickListener {
            startActivity(Intent(this, Setup::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
//            resultLauncher.launch(Intent(mGoogleSignInClient.signInIntent))
        }

        login.setOnClickListener {
            startActivity(Intent(this, com.any1.chat.activities.login::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }
//    private fun createRequest() {
//        // Configure Google Sign In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.clientid))
//            .requestEmail()
//            .build()
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this,
//                OnCompleteListener<AuthResult?> { task ->
//                    if (task.isSuccessful) {
//                        startActivity(Intent(this,MainActivity::class.java))
//                        editor.putString("login","true")
//                        editor.commit()
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Toast.makeText(this, "Unable to login", Toast.LENGTH_SHORT).show()
//                    }
//                })
//    }

    override fun onPause() {
        super.onPause()
        stopPosition = videoView.getCurrentPosition() //stopPosition is an int
        videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        videoView.seekTo(stopPosition)
        videoView.start() //Or use resume() if it doesn't work. I'm not sure
    }

}