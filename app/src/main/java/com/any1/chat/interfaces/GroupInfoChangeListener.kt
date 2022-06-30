package com.any1.chat.interfaces

import com.any1.chat.models.GroupInfoModel

interface GroupInfoChangeListener {
    fun onGroupInfoChanged(groupInfoModel:GroupInfoModel)
}