package com.any1.chat.adapters

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.any1.chat.R
import com.any1.chat.interfaces.OnConnectClickListener
import com.any1.chat.interfaces.OnMemberClickListener
import com.any1.chat.interfaces.OnMenuClickListener
import com.any1.chat.models.MemberModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.mikhaellopez.circularimageview.CircularImageView
import kotlinx.android.synthetic.main.memberoptionsbottomsheet.*

class MembersAdapter(val context : Context, val onMemberClickListener: OnMemberClickListener, val onMenuClickListener : OnMenuClickListener, val onConnectClickListener: OnConnectClickListener, val isAdmin : Boolean) : RecyclerView.Adapter<MembersAdapter.MessageHolder>() {

    private val membersArrayList = ArrayList<MemberModel>()
    private val membersList = ArrayList<String>()
    private val sharedpref = context.getSharedPreferences(context.packageName+"user", MODE_PRIVATE)
    inner class MessageHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val membername : TextView = itemView.findViewById(R.id.membername)
        val memberusername : TextView = itemView.findViewById(R.id.memberusername)
        val connect : MaterialButton = itemView.findViewById(R.id.groupmemberconnect)
        val groupmenu : ImageView = itemView.findViewById(R.id.groupmenu)
        val imageView : CircularImageView = itemView.findViewById(R.id.memberpfp)

        init {
            memberusername.setOnClickListener {
                onMemberClickListener.onGroupMemberClicked(adapterPosition,membersArrayList)
            }
            membername.setOnClickListener {
                onMemberClickListener.onGroupMemberClicked(adapterPosition,membersArrayList)
            }
            imageView.setOnClickListener{
                onMemberClickListener.onGroupMemberClicked(adapterPosition,membersArrayList)
            }

            groupmenu.setOnClickListener{
                onMenuClickListener.onMenuClicked(adapterPosition,membersArrayList)
            }

            connect.setOnClickListener {
                onConnectClickListener.onConnectClicked(adapterPosition,membersArrayList)
            }

        }
    }

    fun setMembersList(arrayList: ArrayList<MemberModel>){
        membersArrayList.addAll(arrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.membersinglerow,parent,false)
        return MessageHolder(view)
    }

    fun clearList(){
        membersArrayList.clear()
    }

    fun notifychange(){
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.membername.text = membersArrayList[position].membername
        holder.memberusername.text = membersArrayList[position].memberusername
        if(membersArrayList[position].connected){
            holder.connect.backgroundTintList = ContextCompat.getColorStateList(context, R.color.darkgreen)
            holder.connect.text = "Connected"
        }else{
            holder.connect.backgroundTintList = ContextCompat.getColorStateList(context, R.color.fblue)
            holder.connect.text = "Connect"
        }
        if(membersArrayList[position].membername == sharedpref.getString("displayname","") && membersArrayList[position].memberusername == sharedpref.getString("username","")){
            holder.connect.visibility = View.INVISIBLE
            holder.groupmenu.visibility = View.INVISIBLE
        }else{
            holder.connect.visibility = View.VISIBLE
            holder.groupmenu.visibility = View.VISIBLE
        }
        if(membersArrayList[position].isAdmin){
            val username = membersArrayList[position].memberusername
            holder.memberusername.text = "Admin • $username"
        }else{
            holder.memberusername.text = membersArrayList[position].memberusername
        }

        if(membersArrayList[position].uri!="" && membersArrayList[position].uri!="male" && membersArrayList[position].uri!="female" ) Glide.with(context).load(membersArrayList[position].uri).circleCrop().into(holder.imageView)
        else if(membersArrayList[position].uri == "male")holder.imageView.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.gigachad))
        else if(membersArrayList[position].uri == "female")holder.imageView.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.doomergirl))
    }

    override fun getItemCount(): Int {
        return membersArrayList.size
    }

}