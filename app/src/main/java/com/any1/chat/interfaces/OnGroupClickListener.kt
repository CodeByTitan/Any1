package com.any1.chat.interfaces

import com.any1.chat.models.GroupModel

interface OnGroupClickListener{
    fun OnGroupClicked(position: Int, groupModelList: ArrayList<GroupModel>)
}