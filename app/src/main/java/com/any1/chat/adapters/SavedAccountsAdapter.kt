package com.any1.chat.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.any1.chat.R
import com.any1.chat.activities.MainActivity
import com.any1.chat.interfaces.AccountClickListener
import com.any1.chat.interfaces.RemoveClickListener
import com.any1.chat.models.SavedAccountModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedAccountsAdapter(val context: Context, val accountClickListener : AccountClickListener, val removeClickListener: RemoveClickListener) : RecyclerView.Adapter<SavedAccountsAdapter.SavedAccountsHolder>() {

    inner class SavedAccountsHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.savedaccountimg)
        val textView = itemView.findViewById<TextView>(R.id.savedaccountusername)
//        val loginbutton = itemView.findViewById<Button>(R.id.login)
        val removebutton = itemView.findViewById<ImageView>(R.id.trash)
        val sharedPreferences = context.getSharedPreferences(context.packageName+"savedaccounts",MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val auth = FirebaseAuth.getInstance()
        init {
//            loginbutton.setOnClickListener {
//                accountClickListener.onAccountClick(arrayList[adapterPosition])
//            }
            textView.setOnClickListener {
                accountClickListener.onAccountClick(arrayList[adapterPosition])
            }
            removebutton.setOnClickListener {
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.removeaccountdialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val remove = dialog.findViewById<TextView>(R.id.remove)
                val cancel = dialog.findViewById<TextView>(R.id.cancel)
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                remove.setOnClickListener {
                    sharedPreferences.edit().putString("count",(arrayList.size-1).toString()).apply()
                    val accountnumber = adapterPosition
                    editor.putString("username$accountnumber","")
                    editor.putString("email$accountnumber","")
                    editor.putString("imgurl$accountnumber","")
                    editor.putString("displayname$accountnumber","")
                    editor.putString("age$accountnumber","")
                    editor.putString("gender$accountnumber","")
                    editor.putString("password$accountnumber","")
                    editor.apply()
                    arrayList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    dialog.dismiss()
                    if(arrayList.size==0){
                        removeClickListener.onTrashClicked()
                    }
                }
                dialog.show()
            }
        }
    }

    private lateinit var arrayList: ArrayList<SavedAccountModel>

    fun setModelList(arrayList: ArrayList<SavedAccountModel>){
        this.arrayList = arrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAccountsHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.singlesavedaccount,parent,false)
        return SavedAccountsHolder(view)
    }

    override fun onBindViewHolder(holder: SavedAccountsHolder, position: Int) {
        val imageurl = arrayList[position].imageurl
        val username = arrayList[position].username
        if(imageurl!="" && imageurl!="male" && imageurl!="female"){
            Glide.with(context).load(imageurl).circleCrop().into(holder.imageView)
        }else{
            if(imageurl=="male"){
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.gigachad))
            }else if(imageurl=="female"){
                holder.imageView.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.doomergirl))
            }
        }
        holder.textView.text = username
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}