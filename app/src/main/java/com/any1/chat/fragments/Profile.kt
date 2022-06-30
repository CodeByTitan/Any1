package com.any1.chat.fragments

import android.Manifest
import android.app.ActivityOptions
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.any1.chat.R
import com.any1.chat.activities.EditProfile
import com.any1.chat.activities.MainActivity
import com.any1.chat.activities.Setup
import com.any1.chat.activities.login
import com.any1.chat.adapters.ChangeAccountAdapter
import com.any1.chat.interfaces.AccountClickListener
import com.any1.chat.models.SavedAccountModel
import com.any1.chat.viewmodels.GroupViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.addaccountdialog.*
import kotlinx.android.synthetic.main.changeaccountbottomsheet.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.w3c.dom.Text
import java.io.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class profile : Fragment(), AccountClickListener{

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var imageStream: InputStream
    private lateinit var bitmap: Bitmap
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var downloadUrl: String? = null
    var picUri: Uri? = null
    var imageurl : String = ""
    private lateinit var pfp : ImageView
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var storageReference: StorageReference? = null
    private lateinit var dialog: Dialog
    var mGetContent: ActivityResultLauncher<String>? = null
    private lateinit var editprofile : ImageView
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var getImageFromCamera: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        pfp = view.findViewById<ImageView>(R.id.circularImageView)
        val displayname = view.findViewById<TextView>(R.id.displayname)
        val toolbar = view.findViewById<Toolbar>(R.id.phonetoolbar)
        sharedPreferences = requireContext().getSharedPreferences(requireContext().packageName+"user", MODE_PRIVATE)
        val username = sharedPreferences.getString("username","")
        val displaynametext = sharedPreferences.getString("displayname","")
        val imgurl = sharedPreferences.getString("imgurl","")
        if(username=="" && displaynametext == "" && imgurl == ""){
            firestore.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
                editor = sharedPreferences.edit()
                editor.putString("imgurl",it.getString("imageurl"))
                editor.putString("username",it.getString("username"))
                editor.putString("age",it.get("age").toString())
                editor.putString("birthdate",it.getString("birthdate"))
                editor.putString("displayname",it.getString("displayname"))
                editor.putString("email",it.getString("email"))
                editor.apply()
            }
        }
        storageReference = FirebaseStorage.getInstance().reference
        val vp = requireActivity().findViewById<View>(R.id.viewpager) as ViewPager2
        vp.isUserInputEnabled = false
        editprofile = view.findViewById(R.id.editprofile)
        editprofile.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfile::class.java)
            intent.putExtra("groupinfo", "groupinfo")
            val bndlAnimation = ActivityOptions.makeCustomAnimation(
                requireContext(),
                R.anim.animenterslow,
                R.anim.animexitslow
            ).toBundle()
            startActivity(intent,bndlAnimation)
        }
        val switchaccount = view.findViewById<ImageView>(R.id.switchaccount)
        switchaccount.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences(requireContext().packageName+"addedaccounts", MODE_PRIVATE)
            dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.changeaccountbottomsheet)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.Dialoganimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
            dialog.addaccount.setOnClickListener {
                dialog.dismiss()
                showAddAccountDialog()
            }
            dialog.plus.setOnClickListener {
                dialog.dismiss()
                showAddAccountDialog()
            }
            dialog.changeaccountrecyclerview.layoutManager = LinearLayoutManager(requireContext())
            var numberofaddedaccounts = 0
            if(sharedPreferences.getString("count","")!=""){
            numberofaddedaccounts = sharedPreferences.getString("count","")!!.toInt()}
            val modelList = ArrayList<SavedAccountModel>()
            if(numberofaddedaccounts == 0){
//                val sharedPreferences = requireContext().getSharedPreferences("user", MODE_PRIVATE)
//                val username = sharedPreferences.getString("username","").toString()
//                val imageurl = sharedPreferences.getString("imgurl","").toString()
//                val displayname = sharedPreferences.getString("displayname","").toString()
//                val age = sharedPreferences.getString("age","").toString()
//                val gender = sharedPreferences.getString("gender","").toString()
//                val email = sharedPreferences.getString("email","").toString()
//                val password = sharedPreferences.getString("password","").toString()
//                val model = SavedAccountModel(username,imageurl,email,password,displayname,age,gender)
                val accountnumber = numberofaddedaccounts + 1
                val editor = sharedPreferences.edit()
                val sp = requireContext().getSharedPreferences(requireContext().packageName+"user", MODE_PRIVATE)
                editor.putString("imgurl$accountnumber",sp.getString("imgurl",""))
                editor.putString("count",accountnumber.toString()).apply()
                editor.putString("username$accountnumber",sp.getString("username",""))
                editor.putString("email$accountnumber",sp.getString("email",""))
                editor.putString("imgurl$accountnumber",sp.getString("imgurl",""))
                editor.putString("displayname$accountnumber",sp.getString("displayname",""))
                editor.putString("age$accountnumber",sp.getString("age",""))
                editor.putString("gender$accountnumber",sp.getString("gender",""))
                editor.putString("password$accountnumber",sp.getString("password",""))
                editor.apply()
            }
            if(sharedPreferences.getString("count","")!=""){
                numberofaddedaccounts = sharedPreferences.getString("count","")!!.toInt()}
//            Toast.makeText(requireContext(), numberofaddedaccounts.toString(), Toast.LENGTH_SHORT).show()
            for(j in 1..numberofaddedaccounts){
                val i = j-1
                val username = sharedPreferences.getString("username$j","").toString()
//                Toast.makeText(context, username, Toast.LENGTH_SHORT).show()
                val imageurl = sharedPreferences.getString("imgurl$j","").toString()
                val displayname = sharedPreferences.getString("displayname$j","").toString()
//                Toast.makeText(context, displayname, Toast.LENGTH_SHORT).show()
                val age = sharedPreferences.getString("age$j","").toString()
                val gender = sharedPreferences.getString("gender$j","").toString()
                val email = sharedPreferences.getString("email$j","").toString()
                val password = sharedPreferences.getString("password$j","").toString()
                val model = SavedAccountModel(username,imageurl,email,password,displayname,age,gender)
                modelList.add(index =i,model)
            }
            dialog.changeaccountrecyclerview.adapter = ChangeAccountAdapter(requireContext(),modelList,this)
            dialog.show()
        }
//        val sharedPreferences2 = requireActivity().getSharedPreferences("theme", MODE_PRIVATE)
//        if(sharedPreferences2.getString("theme","")=="dark"){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            Toast.makeText(context, "nightmode", Toast.LENGTH_SHORT).show()
//        }else{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            Toast.makeText(context, "lightmode", Toast.LENGTH_SHORT).show()
//        }
        val imageUrl = sharedPreferences.getString("imgurl", "")
        val settings = view.findViewById<ImageView>(R.id.settings)
        settings.setOnClickListener {
            startActivity(Intent(activity, com.any1.chat.activities.settings::class.java))
        }
        when {
            imageUrl.toString() == "male" -> {
                imageStream = this.resources.openRawResource(R.raw.gigachad)
                bitmap = BitmapFactory.decodeStream(imageStream)
                pfp.setImageBitmap(bitmap)
            }
            imageUrl.toString() == "female" -> {
                imageStream = this.resources.openRawResource(R.raw.doomergirl)
                bitmap = BitmapFactory.decodeStream(imageStream)
                pfp.setImageBitmap(bitmap)
            }
            else -> {
                Glide.with(requireContext()).load(imageUrl).circleCrop().into(pfp)
            }
        }
        pfp.setOnClickListener {
            showfirstDialog()
        }
        displayname.text = sharedPreferences.getString("displayname", "")
        toolbar.title = sharedPreferences.getString("username", "")
        mGetContent = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { result ->
            if (result != null) {
                dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.progressbardialog)
                val pleasewait = dialog.findViewById<TextView>(R.id.pleasewait)
                pleasewait.text = "Setting Profile Pic"
                dialog.setCancelable(false)
                dialog.show()
                imageurl = result.toString()
//                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,result);
//                createDirectoryAndSaveFile(bitmap,firebaseAuth.currentUser!!.uid)
                Glide.with(requireContext()).load(result).circleCrop().into(pfp)
                uploadToFirebase(result)
            }
//            next.visibility = View.VISIBLE
//            skip.visibility = View.INVISIBLE

        }
        getImageFromCamera = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.data != null) {
                val extras = result.data!!.extras
                if (extras != null) {
                    dialog = Dialog(requireContext())
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.progressbardialog)
                    val pleasewait = dialog.findViewById<TextView>(R.id.pleasewait)
                    pleasewait.text = "Setting Profile Pic"
                    dialog.setCancelable(false)
                    dialog.show()
//                    val extrauri = extras["data"] as Uri?
                    val imageBitmap = extras["data"] as Bitmap?
                    val filepath = context?.let { saveQualityImage(imageBitmap!!, it) }
                    Glide.with(requireContext()).load(filepath).circleCrop().into(pfp)
                    uploadToFirebase(filepath)
                    imageurl = filepath.toString()

                }
            }
        }
        return view
    }

    private fun uploadToFirebase(uri: Uri?) {
        if (uri != null) {
            val uId: String = firebaseAuth.currentUser!!.uid
            val fileRef: StorageReference =
                storageReference?.child("profileImages")!!.child("$uId.jpeg")
            val uploadTask = fileRef.putFile(uri)
            //        Task<Uri> uriTask=
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                downloadUrl = fileRef.downloadUrl.toString()
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                picUri = task.result
                downloadUrl = picUri.toString()
                editor = sharedPreferences.edit()
                editor.putString("imgurl", downloadUrl)
                editor.apply()
                FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.currentUser!!.uid).update(
                    "imageurl",downloadUrl
                )
                dialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Uploading Failed !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
//        final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
    }
        private fun saveQualityImage(bm: Bitmap, context: Context): Uri? {
            val imagesFolder = File(context.cacheDir, "images")
            var uri: Uri? = null
            try {
                imagesFolder.mkdirs()
                val file = File(imagesFolder, firebaseAuth.currentUser!!.uid + ".jpeg")
                val stream = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                uri = FileProvider.getUriForFile(
                    context.applicationContext,
                    "com.any1.chat" + ".provider",
                    file
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return uri
        }

        private val askcamerapermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(
                    context,
                    "We require permission to access camera of your device in order to set your profile photo." +
                            " To setup your profile photo, go to Settings-> Apps -> Stucon -> Permissions -> grant camera permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun openCamera() {
//        String filename = userID;
//        Uri imageUri = null;
//
//        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        try{
//            File imageFile = File.createTempFile(filename,".jpg",storageDirectory);
//            currentPhotoPath = imageFile.getAbsolutePath();
//            imageUri = FileProvider.getUriForFile(editprof.this,"com.example.stucon.fileprovider",imageFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //        camera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//
//            val output = File(dir, "CameraContentDemo.jpeg")
//            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output))
        getImageFromCamera!!.launch(camera)
    }

    private fun showAddAccountDialog(){
        val addaccountdialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
        addaccountdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        addaccountdialog.setContentView(R.layout.addaccountdialog)
        addaccountdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addaccountdialog.window!!.attributes.windowAnimations = R.style.Dialoganimation
        addaccountdialog.window!!.setGravity(Gravity.BOTTOM)
        addaccountdialog.logintoexistingaccount.setOnClickListener {
            val intent = Intent(requireActivity(),login::class.java)
            intent.putExtra("add","add")
            startActivity(intent)
        }
        addaccountdialog.createnewaccount.setOnClickListener {
            val intent = Intent(requireActivity(),Setup::class.java)
            intent.putExtra("add","add")
            startActivity(intent)
        }
        addaccountdialog.show()
    }

//    private fun openCamera() {
//        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val imagePath = createImage()
//        camera.putExtra(MediaStore.EXTRA_OUTPUT,imagePath)
//        getImageFromCamera!!.launch(camera)
//    }

    private fun createImage(): Uri{
        var uri : Uri? = null
        val resolver = requireContext().contentResolver
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val imageName = System.currentTimeMillis().toString()
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,"$imageName.jpg")
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/"+"Any1/")
        val finalUri : Uri = resolver.insert(uri,contentValues)!!
        return finalUri
    }

        private val askgallerypermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                mGetContent!!.launch("image/*")
            } else {
                Toast.makeText(
                    context,
                    "We require permission to access files and media on your device in order to set your profile photo."
                            + " To setup your profile photo, go to Settings-> Apps -> Any1 -> Permissions -> grant Files and media permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun showfirstDialog() {
        val dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.pfpdialog69)
        val newphoto = dialog.findViewById<TextView>(R.id.newphoto)
        val removephoto = dialog.findViewById<TextView>(R.id.removephoto)
        newphoto?.setOnClickListener {
            dialog.dismiss()
            showDialog()
        }
        removephoto?.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences(requireContext().packageName+"user", MODE_PRIVATE)
            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            if(sharedPreferences.getString("gender","")=="male"){
                sharedPreferences.edit().putString("imgurl","male").apply()
                pfp.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.gigachad))
                firestore.collection("users").document(auth.currentUser!!.uid).update("imageurl","male").addOnSuccessListener {
                    dialog.dismiss()
                }
            }else{
                sharedPreferences.edit().putString("imgurl","female").apply()
                pfp.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.doomergirl))
                firestore.collection("users").document(auth.currentUser!!.uid).update("imageurl","female").addOnCanceledListener {
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.Dialoganimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }
        private fun showDialog() {
            val dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.pfpdialog)
            val camera = dialog.findViewById<TextView>(R.id.camera)
            val gallery = dialog.findViewById<TextView>(R.id.materialTextView2)
            camera?.setOnClickListener {
                dialog.dismiss()
                Dexter.withContext(context)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            openCamera()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            askcamerapermission.launch(Manifest.permission.CAMERA)
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
            }

            gallery?.setOnClickListener {
                dialog.dismiss()
                Dexter.withContext(context)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            mGetContent!!.launch("image/*")
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            askgallerypermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
            }
            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = R.style.Dialoganimation
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }

//    private fun createDirectoryAndSaveFile(imageToSave: Bitmap, fileName: String) {
//        val direct = File(Environment.getExternalStorageDirectory().toString() + "/Any1")
//        if (!direct.exists()) {
//            val wallpaperDirectory = File("/sdcard/Any1/")
//            wallpaperDirectory.mkdirs()
//        }
//        val file = File("/sdcard/Any1/", fileName)
//        if (file.exists()) {
//            file.delete()
//        }
//        try {
//            val out = FileOutputStream(file)
//            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

        companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment profile.
             */
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                profile().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }

    override fun onAccountClick(savedAccountModel: SavedAccountModel) {
        val userpref = requireContext().getSharedPreferences(requireContext().packageName+"user", MODE_PRIVATE)
        if(userpref.getString("email","")!=savedAccountModel.email) {
        val dialog2 = Dialog(requireContext())
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog2.setContentView(R.layout.signingin)
        dialog2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.findViewById<TextView>(R.id.signintext).text = "Switching Accounts"
        dialog2.show()
            firebaseAuth.signInWithEmailAndPassword(
                savedAccountModel.email,
                savedAccountModel.password
            ).addOnSuccessListener {
                val editor = userpref.edit()
                editor.putString("username", savedAccountModel.username)
                editor.putString("displayname", savedAccountModel.displayname)
                editor.putString("imgurl", savedAccountModel.imageurl)
                editor.putString("age", savedAccountModel.age)
                editor.putString("gender", savedAccountModel.gender)
                editor.putString("email", savedAccountModel.email)
                editor.putString("password", savedAccountModel.password)
                editor.apply()
                val viewModel = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)
                viewModel.updateAuthId(firebaseAuth.currentUser!!.uid)
                dialog.dismiss()
                val intent = Intent(requireActivity(), MainActivity::class.java)
                dialog2.dismiss()
                intent.putExtra("switch","switch")
                requireActivity().finish()
                startActivity(intent)
            }
        }
    }
//    var fOut: OutputStream? = null
//    val file = File(imagePath, "GE_" + System.currentTimeMillis() + ".jpg")

//    try {
//        fOut = FileOutputStream(file)
//    } catch (e: FileNotFoundException) {
//        e.printStackTrace()
//    }
//
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
//    try {
//        fOut!!.flush()
//        fOut!!.close()
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//
//    val values = ContentValues()
//    values.put(Images.Media.TITLE, this.getString(R.string.picture_title))
//    values.put(Images.Media.DESCRIPTION, this.getString(R.string.picture_description))
//    values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis())
//    values.put(
//    Images.ImageColumns.BUCKET_ID,
//    file.toString().toLowerCase(Locale.US).hashCode()
//    )
//    values.put(
//    Images.ImageColumns.BUCKET_DISPLAY_NAME,
//    file.name.toLowerCase(Locale.US)
//    )
//    values.put("_data", file.absolutePath)
//
//    val cr: ContentResolver = getContentResolver()
//    cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
}