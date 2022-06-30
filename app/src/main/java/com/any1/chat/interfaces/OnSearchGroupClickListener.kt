package com.any1.chat.interfaces

import com.any1.chat.models.GroupModel
import com.any1.chat.models.SearchModel

interface OnSearchGroupClickListener {
    fun OnGroupClicked(position: Int, groupModelList: ArrayList<SearchModel>)
}