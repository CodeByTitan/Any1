package com.any1.chat.interfaces

import com.any1.chat.models.SavedAccountModel

interface AccountClickListener {
    fun onAccountClick(savedAccountModel: SavedAccountModel)
}