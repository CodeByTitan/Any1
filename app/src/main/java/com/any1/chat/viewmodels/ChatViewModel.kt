package com.any1.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.any1.chat.interfaces.MessageReceiveListener
import com.any1.chat.models.ChatModel
import com.any1.chat.repository.ChatRepository


class ChatViewModel: ViewModel(),MessageReceiveListener{
    private val mutableLiveData: MutableLiveData<ArrayList<ChatModel>?> = MutableLiveData<ArrayList<ChatModel>?>()
    var getGroupMessages : LiveData<ArrayList<ChatModel>?> = mutableLiveData
    val chatRepository = ChatRepository(this)
    fun getMessages(grouptag : String){
        chatRepository.getMessages(grouptag)
    }

    override fun OnMessageReceived(messageModels: ArrayList<ChatModel>) {
        mutableLiveData.value = messageModels
    }

    fun stopReceiveingMessages() {
        mutableLiveData.postValue(null)
    }
}