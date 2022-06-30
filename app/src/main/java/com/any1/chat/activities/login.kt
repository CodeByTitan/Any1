package com.any1.chat.activities

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.any1.chat.R
import com.any1.chat.viewmodels.GroupViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream


class login : AppCompatActivity() {

    private lateinit var loginbutton : Button
    private lateinit var loginprogress : ProgressBar
    private var usernamestr = ""
    private var passwordstr = ""
    private lateinit var animation: Animation
    private lateinit var animation2: Animation
    private lateinit var arrow : ImageView
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var uninput : TextInputLayout
    private lateinit var passinput : TextInputLayout
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences2 = getSharedPreferences(packageName+"theme", MODE_PRIVATE)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        if(sharedPreferences2.getString("theme","")=="dark"){
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        }else{
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.loginusername)
        val savedaccounts = getSharedPreferences(packageName+"savedaccounts", MODE_PRIVATE)
//        savedaccounts.edit().putString("count","0").apply()
//        val editor = savedaccounts.edit()
//        val accountnumber = 2
//        editor.putString("username$accountnumber",sharedPreferences.getString("username",""))
//        editor.putString("saveinfo$accountnumber","true").apply()
//        editor.putString("email$accountnumber",sharedPreferences.getString("email",""))
//        editor.putString("imgurl$accountnumber",sharedPreferences.getString("imgurl",""))
//        editor.putString("displayname$accountnumber",sharedPreferences.getString("displayname",""))
//        editor.putString("age$accountnumber",sharedPreferences.getString("age",""))
//        editor.putString("gender$accountnumber",sharedPreferences.getString("gender",""))
//        editor.putString("password$accountnumber",sharedPreferences.getString("password",""))
//        editor.apply()
        password = findViewById<EditText>(R.id.loginpassword)
        val gethelp = findViewById<TextView>(R.id.gethelp)
        val text : TextView = findViewById(R.id.text)
        loginbutton = findViewById<Button>(R.id.loginbutton)
        arrow = findViewById<ImageView>(R.id.arrow)
        val numberofsavedaccounts = savedaccounts.getString("count","")
        if(intent.getStringExtra("email")!=""){
            username.setText(intent.getStringExtra("email"))
            usernamestr = username.text.toString()
        }
//        val userimage : ImageView = findViewById(R.id.userimage)
//        val crossbutton : ImageView = findViewById(R.id.crossbutton
//
        animation = AnimationUtils.loadAnimation(this,R.anim.bouncetestanimation)
        animation2 = AnimationUtils.loadAnimation(this,R.anim.sampleanimation)
        arrow.setOnClickListener {
            val intent1 = Intent(this,com.any1.chat.activities.savedaccounts::class.java)
            if(intent.getStringExtra("add")=="add")intent1.putExtra("add","add")
            startActivity(intent1)
            overridePendingTransition(R.anim.slide_in_bottom,R.anim.slide_out_top)
        }
        if(numberofsavedaccounts=="" || numberofsavedaccounts=="0"){
            arrow.visibility = View.INVISIBLE
            text.visibility = View.INVISIBLE
        }else{
            arrow.visibility = View.VISIBLE
            text.visibility = View.VISIBLE
            animateArrow()
        }
        if(passwordstr == "" || usernamestr ==""){
            loginbutton.isEnabled = false
            loginbutton.alpha = 0.5f
//                loginbutton.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.GRAY,BlendModeCompat.SRC_ATOP)
//                loginbutton.background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        }else {loginbutton.isEnabled = true
            loginbutton.alpha = 1f
//                loginbutton.background.colorFilter = null
        }
        username.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener {
            val r = Rect()
            arrow.getWindowVisibleDisplayFrame(r)
            val heightDiff: Int = arrow.rootView.height - (r.bottom - r.top)
            if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                //ok now we know the keyboard is up...
                arrow.visibility = View.GONE
                text.visibility = View.GONE
                arrow.clearAnimation()
            } else {
                //ok now we know the keyboard is down...
                if(numberofsavedaccounts=="" || numberofsavedaccounts=="0"){
                    arrow.visibility = View.INVISIBLE
                    text.visibility = View.INVISIBLE
                }else{
                    arrow.visibility = View.VISIBLE
                    text.visibility = View.VISIBLE
                    animateArrow()
                }
            }
        })
//        animation.repeatCount = Animation.INFINITE
//        arrow.startAnimation(animation)
//        ObjectAnimator.ofFloat(arrow, "translationY", 50f).apply {
//            duration = 2000
//            start()
//            repeatCount = Animation.INFINITE
//        }

//
//        arrow.animate().translationY(25f)
//            .setDuration(1000).withEndAction {
//                arrow.animate().translationY(-25f).setDuration(1000).start()
//            }.start()

        uninput = findViewById(R.id.uninput)
        passinput = findViewById(R.id.passinput)
        loginbutton.setOnClickListener {
//            if(savepreferences.getString("saveinfo","")=="false"){
                loginUser()
//            }else{
//                val dialog = Dialog(this)
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                dialog.setContentView(R.layout.signingin)
//                dialog.setCancelable(false)
//                dialog.show()
//                val email = sharedPreferences.getString("email","")
//                val savedpassword = sharedPreferences.getString("password","")
//                if(email != "" && savedpassword!= ""){
//                    auth.signInWithEmailAndPassword(email.toString(), savedpassword.toString()).addOnSuccessListener {
//                        val sharedPreferences = getSharedPreferences(
//                            "login",
//                            MODE_PRIVATE
//                        )
//                        sharedPreferences.edit().putString("login", "true")
//                            .apply()
//                        dialog.dismiss()
//                        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_ANIMATION))
//                    }
//                }else{
//                    Toast.makeText(this, "Email or Password is null", Toast.LENGTH_SHORT).show()
//                }
//            }

        }
//        if(numberofsavedaccounts!="" && numberofsavedaccounts!="0"){
//            loginbutton.text = "Login as $name"
//            loginbutton.isEnabled = true
//            loginbutton.alpha = 1f
//            val imageuristr = sharedPreferences.getString("imgurl","")
//            var imageDrawable : Drawable
//            if(imageuristr == "male"|| imageuristr =="female"){
//                if(imageuristr == "male"){
//                    val imageStream = this.resources.openRawResource(R.raw.gigachad)
//                    val bitmap = BitmapFactory.decodeStream(imageStream)
////                    val uri = getImageUri(this,bitmap)
//                    val d: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources,  Bitmap.createScaledBitmap(bitmap, 75, 75, true))
//                    d.isCircular = true
//                    userimage.setImageURI(uri)
//                    userimage.setImageDrawable(d)

//                }
//                else{
//                    val imageStream = this.resources.openRawResource(R.raw.doomergirl)
//                    val bitmap = BitmapFactory.decodeStream(imageStream)
//                    val d: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources,  Bitmap.createScaledBitmap(bitmap, 75, 75, true))
//                    d.isCircular = true
//                    userimage.setImageURI(uri)
//                    userimage.setImageDrawable(d)
//                }

//            }
//            else{
////                val uri = imageuristr!!.toUri()
////                val uri:Uri =  File().to1Uri()
////                val inputStream = contentResolver.openInputStream(uri)
////                val bitmap = BitmapFactory.decodeStream(inputStream)
////                val d: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources,  Bitmap.createScaledBitmap(bitmap, 75, 75, true))
////                d.isCircular = true
////                    userimage.setImageURI(uri)
////
////                Glide.with(this).load(uri).circleCrop().into(userimage)
//
//            }
//
//            crossbutton.setOnClickListener {
//                savepreferences.edit().putString("saveinfo", "false").apply()
//                sharedPreferences.edit().clear().apply()
//                crossbutton.visibility = View.INVISIBLE
//                userimage.visibility = View.INVISIBLE
//                loginbutton.setPadding(0, 0, 0, 0)
//                loginbutton.text = "Login"
//            }
//        }else{
//            crossbutton.visibility = View.INVISIBLE
//            userimage.visibility = View.INVISIBLE
//            loginbutton.isEnabled = false
//            loginbutton.alpha = 0.5f
//            loginbutton.setPadding(0,0,0,0)
//            loginbutton.text = "Login"
//            loginbutton.setOnClickListener{
//                loginUser()
//            }
//        }
//        val sharedPreferences2 = getSharedPreferences("theme", MODE_PRIVATE)
        //ToDo - try running login and fix changing theme
        password.transformationMethod = PasswordTransformationMethod.getInstance()
        username.doAfterTextChanged {
                text: Editable? -> usernamestr = text.toString()
            uninput.error=null
            if(passwordstr == "" || usernamestr ==""){
                loginbutton.isEnabled = false
                loginbutton.alpha = 0.5f
//                loginbutton.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.GRAY,BlendModeCompat.SRC_ATOP)
//                loginbutton.background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
            }else {loginbutton.isEnabled = true
                loginbutton.alpha = 1f
//                loginbutton.background.colorFilter = null
            }
        }
        password.doAfterTextChanged { text: Editable? -> passwordstr = text.toString()
        passinput.error = null
            if(passwordstr == "" || usernamestr ==""){
                loginbutton.isEnabled = false
//                loginbutton.background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.GRAY,BlendModeCompat.SRC_ATOP)
//                loginbutton.background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
                loginbutton.alpha = 0.5f
            }else {loginbutton.isEnabled = true
                loginbutton.alpha = 1f
//                loginbutton.background.colorFilter = null
            }

        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_background)
        loginprogress = findViewById<ProgressBar>(R.id.loginprogress)
        loginprogress.visibility = View.INVISIBLE

        gethelp.setOnClickListener{
            val intent = Intent(this, forgotpass::class.java)
            val bndlAnimation = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.anim_enter,
                R.anim.anim_exit
            ).toBundle()
            startActivity(intent, bndlAnimation)
            loginprogress.visibility = View.INVISIBLE
            loginbutton.text = "Login"
        }

    }
    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun showUsernameDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.incorrectusernamedialog)
        dialog.findViewById<TextView>(R.id.tryagain).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showEmailDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.incorrectusernamedialog)
        dialog.findViewById<TextView>(R.id.incorrectps).text = "Incorrect Email"
        dialog.findViewById<TextView>(R.id.incorrectusernametext).text = "The email you entered does not appear to belong to an account. Please re-check the email and try again "
        dialog.findViewById<TextView>(R.id.tryagain).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun showPasswordDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.incorrectpassworddialog)
        dialog.findViewById<TextView>(R.id.ok).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun loginUser(){
        var email = ""
        if(usernamestr != ""|| passwordstr != "") {
            loginbutton.text = ""
            loginprogress.visibility = View.VISIBLE
            firestore.collection("usernames").document(usernamestr).get()
                .addOnSuccessListener { coll ->
                    if (coll.exists()) {
                        email = coll.getString("email").toString()
                        val dialog = Dialog(this)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.signingin)
                        dialog.setCancelable(false)
                        dialog.show()
                        auth.signInWithEmailAndPassword(email, passwordstr)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firestore.collection("users")
                                        .document(auth.currentUser!!.uid).get()
                                        .addOnSuccessListener { document ->
                                            saveInformation(document,email,dialog)
                                        }

                                } else {
                                    dialog.dismiss()
                                   showPasswordDialog()
                                    loginprogress.visibility = View.INVISIBLE
                                    loginbutton.text = "Login"
                                }
                            }.addOnFailureListener {
                                dialog.dismiss()
                                showPasswordDialog()
                                loginprogress.visibility = View.INVISIBLE
                                loginbutton.text = "Login"
                            }
                    } else {
                        if (usernamestr.isValidEmail()) {
                            auth.fetchSignInMethodsForEmail(usernamestr).addOnSuccessListener {
                                auth.signInWithEmailAndPassword(usernamestr, passwordstr)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val dialog = Dialog(this)
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                            dialog.setContentView(R.layout.signingin)
                                            dialog.setCancelable(false)
                                            dialog.show()
                                            firestore.collection("users")
                                                .document(auth.currentUser!!.uid).get()
                                                .addOnSuccessListener { document ->
                                                    saveInformation(document,email,dialog)
                                                }.addOnFailureListener {
                                                    loginprogress.visibility = View.INVISIBLE
                                                    loginbutton.text = "Login"
                                                    dialog.dismiss()
                                                    Toast.makeText(
                                                        this,
                                                        "Sign in failed",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }

                                        } else {
                                            loginprogress.visibility = View.INVISIBLE
                                            loginbutton.text = "Login"
//                                            passinput.error = "Password is incorrect"
                                            showPasswordDialog()
                                        }
                                    }

                            }.addOnFailureListener {
                                loginprogress.visibility = View.INVISIBLE
                                loginbutton.text = "Login"
                                showEmailDialog()
                            }
                        }else{
                            loginprogress.visibility = View.INVISIBLE
                            loginbutton.text = "Login"
//                            uninput.error = "Username is Incorrect"
                            showUsernameDialog()
                        }
                    }
                }
        }else{
            loginprogress.visibility = View.INVISIBLE
            loginbutton.text = "Login"
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveInformation(document : DocumentSnapshot,email : String, dialog : Dialog ){
        if(intent.getStringExtra("add")=="add") {
            val sharedPreferences = getSharedPreferences(packageName+"addedaccounts", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            var numberofaddedaccounts = 0
            if (sharedPreferences.getString("count", "") != "") numberofaddedaccounts =
                sharedPreferences.getString("count", "")!!.toInt()
            var isAccountAdded = false
            if (numberofaddedaccounts != 0) {
                for (i in 1..numberofaddedaccounts) {
                    val username = sharedPreferences.getString("username$i", "")
                    if (username == document.getString("username")) {
                        isAccountAdded = true
                    }
                }
            }
            if (!isAccountAdded) {
                val accountnumber = numberofaddedaccounts + 1
                editor.putString("count", accountnumber.toString()).apply()
                editor.putString("username$accountnumber", document.getString("username"))
                editor.putString("email$accountnumber", document.getString("email"))
                editor.putString("imgurl$accountnumber", document.getString("imageurl"))
                editor.putString("displayname$accountnumber", document.getString("displayname"))
                editor.putString("age$accountnumber", document.get("age").toString())
                editor.putString("gender$accountnumber", document.getString("gender"))
                editor.putString("password$accountnumber", passwordstr)
                editor.apply()
                val viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
                viewModel.updateAuthId(auth.currentUser!!.uid)
            }
        }
        val sharedPreferences = getSharedPreferences(packageName+"user", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(
            "displayname",
            document.getString("displayname")
        ).apply()
        editor.putString(
            "imgurl",
            document.getString("imageurl")
        ).apply()
        editor.putString(
            "username",
            document.getString("username")
        ).apply()
        editor.putString(
            "birthdate",
            document.getString("birthdate")
        ).apply()
        editor.putString(
            "age",
            document.get("age").toString()
        ).apply()
        editor.putString(
            "gender",
            document.getString("gender")
        ).apply()
        editor.putString("email", email).apply()
        editor.putString("password",passwordstr).apply()
        editor.apply()
        dialog.dismiss()
        if(intent.getStringExtra("add")=="add") {
            val viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
            viewModel.updateAuthId(auth.currentUser!!.uid)
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
            finish()
        }else{
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
        val sp = getSharedPreferences(
            packageName+"login",
            MODE_PRIVATE
        )
        sp.edit().putString("login", "true")
            .apply()
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun animateArrow(){
        if(arrow.visibility !=View.INVISIBLE && arrow.visibility!= View.GONE){
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {
                    arrow.startAnimation(animation2)
                }
            })
            animation2.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {}
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {
                    arrow.startAnimation(animation)}
            })
            arrow.startAnimation(animation)
        }
    }
}
//.addOnFailureListener {
//    if (usernamestr.isValidEmail()) {
//        auth.fetchSignInMethodsForEmail(usernamestr).addOnSuccessListener {
//            auth.signInWithEmailAndPassword(usernamestr, passwordstr)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val dialog = Dialog(this)
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                        dialog.setContentView(R.layout.signingin)
//                        dialog.show()
//                        firestore.collection("users")
//                            .document(auth.currentUser!!.uid).get()
//                            .addOnSuccessListener { document ->
//                                editor.putString(
//                                    "displayname",
//                                    document.getString("displayname")
//                                )
//                                editor.putString(
//                                    "imgurl",
//                                    document.getString("imageurl")
//                                )
//                                editor.putString(
//                                    "username",
//                                    document.getString("username")
//                                )
//                                editor.putString(
//                                    "birthdate",
//                                    document.getString("imageurl")
//                                )
//                                editor.putString(
//                                    "age",
//                                    document.getString("imageurl")
//                                )
//                                editor.putString("email", email)
//                                editor.putString(
//                                    "gender",
//                                    document.getString("gender")
//                                )
//                                editor.apply()
//                                dialog.dismiss()
//                                startActivity(
//                                    Intent(
//                                        this,
//                                        MainActivity::class.java
//                                    )
//                                )
//                                val sharedPreferences = getSharedPreferences(
//                                    "login",
//                                    AppCompatActivity.MODE_PRIVATE
//                                )
//                                sharedPreferences.edit()
//                                    .putString("login", "true").apply()
////                                        loginprogress.visibility = View.INVISIBLE
////                                        loginbutton.text = "Login"
////                                        firestore.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {doc->
////                                            if(doc.exists()){
////
////                                            }
////                                        }
//
//                            }.addOnFailureListener {
//                                loginprogress.visibility = View.INVISIBLE
//                                dialog.dismiss()
//                                Toast.makeText(
//                                    this,
//                                    "Sign in failed",
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }
//
//                    } else {
//                        loginbutton.text = "Login"
//                        loginprogress.visibility = View.INVISIBLE
//                        Toast.makeText(
//                            this,
//                            "Email or Password is incorrect",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        loginprogress.visibility = View.INVISIBLE
//                        loginbutton.text = "Login"
//                    }
//                }.addOnFailureListener {
//                    loginbutton.text = "Login"
//                    loginprogress.visibility = View.INVISIBLE
//                    Toast.makeText(
//                        this,
//                        "Email or Password is incorrect",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    loginprogress.visibility = View.INVISIBLE
//                    loginbutton.text = "Login"
//                }
//
//        }.addOnFailureListener {
//            loginprogress.visibility = View.INVISIBLE
//            loginbutton.text = "Login"
//            Toast.makeText(this, "Email is incorrect", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }else{
//        loginprogress.visibility = View.INVISIBLE
//        loginbutton.text = "Login"
//        Toast.makeText(this, "Username is incorrect", Toast.LENGTH_SHORT).show()
//    }
//}

//                        firestore.collection("users").whereEqualTo("username",usernamestr).get().addOnSuccessListener { documents ->
//                            for (document in documents) {
//                                email = document.getString("email").toString()
//                                if(email!=null) {
//                                    auth.signInWithEmailAndPassword(email, passwordstr)
//                                        .addOnCompleteListener { task ->
//                                            if (task.isSuccessful) {
//                                                editor.putString("displayname",document.getString("displayname"))
//                                                editor.putString("imgurl",document.getString("imageurl"))
//                                                editor.putString("username",usernamestr)
//                                            }
//                                        }
//                                }else{
//
//                                }
//                            }
//                    if(it.exists()){
//                        email = it.getString("email").toString()
//                        auth.signInWithEmailAndPassword(email,passwordstr).addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                editor.putString("displayname",it.getString("displayname"))
//                                editor.putString("imgurl",it.getString("imageurl"))
//                                startActivity(Intent(this, MainActivity::class.java))
//                            } else {
//                                passinput.error = "Password is incorrect"
//                            }
//                        }
//                    }
//                        }.addOnFailureListener{
//                            uninput.error = "Username is not valid"
//                        }