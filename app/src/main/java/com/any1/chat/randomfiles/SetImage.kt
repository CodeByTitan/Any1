package com.any1.chat.randomfiles

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.any1.chat.R
import com.any1.chat.activities.MainActivity
import com.any1.chat.activities.Setup
import com.any1.chat.activities.CreateGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class SetImage{
    var downloadUrl = ""
    val auth = FirebaseAuth.getInstance()
    var mGetContent: ActivityResultLauncher<String>? = null
    var getImageFromCamera: ActivityResultLauncher<Intent>? = null
    private lateinit var picUri : Uri
    private lateinit var dialog :Dialog
    private lateinit var activity : Activity
    private var imageurl = ""
    var askcamerapermission: ActivityResultLauncher<String>? = null
    var askgallerypermission: ActivityResultLauncher<String>? = null
    fun setAndUploadImage(context: Context, int : Int, imageView: ImageView) {
        if(int ==0){
            activity = Setup()
            mGetContent = (activity as Setup).registerForActivityResult(
                ActivityResultContracts.GetContent()
            ) { result ->
                dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.progressbardialog)
                val pleasewait = dialog.findViewById<TextView>(R.id.pleasewait)
                pleasewait.text = "Setting Your Profile Pic"
                dialog.show()
                if (result != null) {
                    imageurl = result.toString()
                    imageView.setImageURI(result)
                    uploadToFirebase(result,context)
                }
//            next.visibility = View.VISIBLE
//            skip.visibility = View.INVISIBLE
                dialog.dismiss()

            }

            getImageFromCamera = (activity as Setup).registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.data != null) {
                    val extras = result.data!!.extras
                    if (extras != null) {
                        val imageBitmap = extras["data"] as Bitmap?
                        val filepath = context?.let { saveQualityImage(imageBitmap!!, it) }
                        imageView.setImageURI(filepath)
                        uploadToFirebase(filepath,context)
//                    next.visibility = View.VISIBLE
//                    skip.visibility = View.INVISIBLE
                        imageurl = filepath.toString()

                    }
                }
            }

            val askcamerapermission = (activity as Setup).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    getImageFromCamera!!.launch(camera)
                } else {
                    Toast.makeText(
                        context,
                        "We require permission to access camera of your device in order to set your profile photo." +
                                " To setup your profile photo, go to Settings-> Apps -> Stucon -> Permissions -> grant camera permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


            //    private void performCrop(Uri uri){
            //        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
            //    }
            val askgallerypermission = (activity as Setup).registerForActivityResult(
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
        }
        else if(int ==1) {
            activity = MainActivity()
            mGetContent = (activity as MainActivity).registerForActivityResult(
                ActivityResultContracts.GetContent()
            ) { result ->
                dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.progressbardialog)
                val pleasewait = dialog.findViewById<TextView>(R.id.pleasewait)
                pleasewait.text = "Setting Your Profile Pic"
                dialog.show()
                if (result != null) {
                    imageurl = result.toString()
                    imageView.setImageURI(result)
                    uploadToFirebase(result,context)
                }
//            next.visibility = View.VISIBLE
//            skip.visibility = View.INVISIBLE
                dialog.dismiss()

            }

            getImageFromCamera = (activity as MainActivity).registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.data != null) {
                    val extras = result.data!!.extras
                    if (extras != null) {
                        val imageBitmap = extras["data"] as Bitmap?
                        val filepath = context?.let { saveQualityImage(imageBitmap!!, it) }
                        imageView.setImageURI(filepath)
                        uploadToFirebase(filepath,context)
//                    next.visibility = View.VISIBLE
//                    skip.visibility = View.INVISIBLE
                        imageurl = filepath.toString()

                    }
                }
            }

            val askcamerapermission = (activity as MainActivity).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    getImageFromCamera!!.launch(camera)
                } else {
                    Toast.makeText(
                        context,
                        "We require permission to access camera of your device in order to set your profile photo." +
                                " To setup your profile photo, go to Settings-> Apps -> Stucon -> Permissions -> grant camera permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


            //    private void performCrop(Uri uri){
            //        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
            //    }
            val askgallerypermission = (activity as MainActivity).registerForActivityResult(
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
            }}
        else if(int ==2) {activity = CreateGroup()
            mGetContent = (activity as CreateGroup).registerForActivityResult(
                ActivityResultContracts.GetContent()
            ) { result ->
                dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.progressbardialog)
                val pleasewait = dialog.findViewById<TextView>(R.id.pleasewait)
                pleasewait.text = "Setting Your Profile Pic"
                dialog.show()
                if (result != null) {
                    imageurl = result.toString()
                    imageView.setImageURI(result)
                    uploadToFirebase(result,context)
                }
//            next.visibility = View.VISIBLE
//            skip.visibility = View.INVISIBLE
                dialog.dismiss()

            }

            getImageFromCamera = (activity as CreateGroup).registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.data != null) {
                    val extras = result.data!!.extras
                    if (extras != null) {
                        val imageBitmap = extras["data"] as Bitmap?
                        val filepath = context?.let { saveQualityImage(imageBitmap!!, it) }
                        imageView.setImageURI(filepath)
                        uploadToFirebase(filepath,context)
//                    next.visibility = View.VISIBLE
//                    skip.visibility = View.INVISIBLE
                        imageurl = filepath.toString()

                    }
                }
            }

            val askcamerapermission = (activity as CreateGroup).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    getImageFromCamera!!.launch(camera)
                } else {
                    Toast.makeText(
                        context,
                        "We require permission to access camera of your device in order to set your profile photo." +
                                " To setup your profile photo, go to Settings-> Apps -> Stucon -> Permissions -> grant camera permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


            //    private void performCrop(Uri uri){
            //        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
            //    }
            val askgallerypermission = (activity as CreateGroup).registerForActivityResult(
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
            }}


        fun showDialog() {
            val dialog = context.let { Dialog(it) }
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
                            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            getImageFromCamera!!.launch(camera)
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            askcamerapermission!!.launch(Manifest.permission.CAMERA)
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
                            askgallerypermission!!.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    }
    private fun uploadToFirebase(uri: Uri?,context: Context) {
        if (uri != null) {
            val uId: String = auth.currentUser!!.uid
            val storageReference = FirebaseStorage.getInstance().reference
            val fileRef: StorageReference =
                storageReference.child("groupImages").child("$uId.jpeg")
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
//                    editor = sharedPreferences!!.edit()
//                    editor?.putString("imgurl", downloadUrl)
//                    editor?.apply()
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
            val file = File(imagesFolder, auth.currentUser!!.uid + ".jpeg")
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


}