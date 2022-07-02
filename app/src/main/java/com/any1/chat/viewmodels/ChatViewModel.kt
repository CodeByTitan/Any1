package com.any1.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.any1.chat.interfaces.MessageReceiveListener
import com.any1.chat.models.ChatModel
import com.any1.chat.repository.ChatRepository
import kotlinx.coroutines.launch


class ChatViewModel: ViewModel(),MessageReceiveListener{
    private val mutableLiveData: MutableLiveData<ArrayList<ChatModel>?> = MutableLiveData<ArrayList<ChatModel>?>()
    var getGroupMessages : MutableLiveData<ArrayList<ChatModel>?> = mutableLiveData
    val chatRepository = ChatRepository(this)

    fun getMessages(grouptag : String){
        viewModelScope.launch {
            chatRepository.getMessages(grouptag)
        }
    }

    override fun OnMessageReceived(messageModels: ArrayList<ChatModel>) {
        mutableLiveData.postValue(messageModels)
    }

    fun stopReceiveingMessages() {
        mutableLiveData.postValue(null)
    }
}