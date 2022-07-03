package com.any1.chat.interfaces

import com.any1.chat.models.ChatModel

interface MessageReceiveListener {
    fun OnMessageReceived(messageModels: ArrayList<ChatModel>)
}