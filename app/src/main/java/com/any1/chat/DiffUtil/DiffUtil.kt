package com.any1.chat.DiffUtil

import androidx.recyclerview.widget.DiffUtil

class DiffUtil(oldarray :ArrayList<String>, newarray : ArrayList<String>) : DiffUtil.Callback() {
    val oldarray = ArrayList<String>()
    val newarray = ArrayList<String>()



    override fun getOldListSize(): Int {
       return oldarray.size
    }

    override fun getNewListSize(): Int {
        return newarray.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldarray[oldItemPosition] == newarray[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return when{
           oldarray[oldItemPosition] != newarray[newItemPosition] ->{
               false
           }
           else -> true
       }
    }
}