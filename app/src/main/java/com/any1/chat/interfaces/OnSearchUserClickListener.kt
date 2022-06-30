package com.any1.chat.interfaces

import com.any1.chat.models.UserModel

interface OnSearchUserClickListener {
    fun onUserClicked(position : Int, userList: ArrayList<UserModel>)
}