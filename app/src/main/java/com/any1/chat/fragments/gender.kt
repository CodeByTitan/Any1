package com.any1.chat.fragments

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.any1.chat.R
import com.any1.chat.viewmodels.AddAccountViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [gender.newInstance] factory method to
 * create an instance of this fragment.
 */
class gender : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gender, container, false)
        val male = view.findViewById<CheckBox>(R.id.male)
        val female = view.findViewById<CheckBox>(R.id.female)
        val sharedPreferences = requireContext().getSharedPreferences(requireContext().packageName+"temporaryuser",MODE_PRIVATE)
        val next = view.findViewById<Button>(R.id.gendernext)
        val auth = FirebaseAuth.getInstance()
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.profiledialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val stopcreation = dialog.findViewById<TextView>(R.id.stopcreating)
                val continuee = dialog.findViewById<TextView>(R.id.continuee)
                continuee.setOnClickListener { dialog.dismiss() }
                stopcreation.setOnClickListener {
                    dialog.dismiss()
                    val username = sharedPreferences.getString("username", "")
                    if (username != null) {
                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("usernames").document(username).get()
                            .addOnSuccessListener {
                                firestore.collection("usernames").document(username).delete()
                                auth.currentUser?.delete()
                            }
                    }
                    requireActivity().finish()

                }
                dialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        next.setOnClickListener {
            if(!male.isChecked && !female.isChecked){
                Toast.makeText(context, "Please Select Your Gender", Toast.LENGTH_SHORT).show()
            }else{
                val navController = requireActivity().findNavController(R.id.fragmentContainerView2)
                navController.navigate(R.id.displaynamepic)
            }
        }
        val editor = sharedPreferences.edit()
        male.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                female.isChecked = false
                editor.putString("gender","male").apply()
            }else{
                female.isChecked = true
                editor.putString("gender","female").apply()
            }
        }

        female.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                male.isChecked = false
                editor.putString("gender","female").apply()
            }else{
                male.isChecked = true
                editor.putString("gender","male").apply()
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment gender.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            gender().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}