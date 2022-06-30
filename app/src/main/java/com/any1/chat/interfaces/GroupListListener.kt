package com.any1.chat.interfaces

import com.any1.chat.models.GroupModel

interface GroupListListener {
    fun showListOfUser(groupModelList: ArrayList<GroupModel>)
}