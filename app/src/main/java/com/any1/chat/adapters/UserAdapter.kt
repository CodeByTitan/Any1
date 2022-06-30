//package com.any1.chat.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.content.res.AppCompatResources
//import androidx.recyclerview.widget.RecyclerView
//import com.any1.chat.R
//import com.any1.chat.interfaces.OnGroupClickListener
//import com.any1.chat.interfaces.OnSearchGroupClickListener
//import com.any1.chat.interfaces.OnSearchUserClickListener
//import com.any1.chat.models.SearchModel
//import com.any1.chat.models.UserModel
//import com.bumptech.glide.Glide

//class UserAdapter(val searchUserClickListener: OnSearchUserClickListener,val context: Context) : RecyclerView.Adapter<UserAdapter.UserHolder>()  {
//
//    private var arrayList : ArrayList<UserModel> = ArrayList()
//    inner class UserHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
//        val textView: TextView = itemView.findViewById(R.id.gcname)
//        val imageView: ImageView = itemView.findViewById(R.id.gcsmallpic)
//        val name : TextView = itemView.findViewById(R.id.name)
//        init {
//            textView.setOnClickListener {
//                searchUserClickListener.onUserClicked(adapterPosition,arrayList)
//            }
//            imageView.setOnClickListener {
//                searchUserClickListener.onUserClicked(adapterPosition,arrayList)
//            }
//            name.setOnClickListener {
//                searchUserClickListener.onUserClicked(adapterPosition,arrayList)
//            }
//        }
//    }
//
//    fun setUserList(arrayList: ArrayList<UserModel>){
//        this.arrayList = arrayList
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
//        val context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val view = inflater.inflate(R.layout.searchsinglerow,parent,false)
//        return UserHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: UserHolder, position: Int) {
//        holder.textView.text = arrayList[position].username
//        if(arrayList[position].uri == "male"){
//            val drawable = AppCompatResources.getDrawable(context,R.drawable.gigachad)
//            holder.imageView.setImageDrawable(drawable)
//        } else if(arrayList[position].uri == "female"){
//            val drawable = AppCompatResources.getDrawable(context,R.drawable.doomergirl)
//            holder.imageView.setImageDrawable(drawable)
//        } else{
//            Glide.with(context).load(arrayList[position].uri).circleCrop().into(holder.imageView)
//        }
//        val name = arrayList[position].name
//        holder.name.text = name
//    }
//
//    override fun getItemCount(): Int {
//        return arrayList.size
//    }
//}