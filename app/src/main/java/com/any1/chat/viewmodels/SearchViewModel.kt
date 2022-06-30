package com.any1.chat.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.any1.chat.models.SearchModel
import com.any1.chat.models.SearchUserModel
import com.any1.chat.models.UserModel
import com.any1.chat.repository.SearchRepository

class SearchViewModel: ViewModel() {
    private val mutableLiveData : MutableLiveData<ArrayList<SearchModel>> = MutableLiveData()
    private val mutableUserList : MutableLiveData<ArrayList<SearchUserModel>> = MutableLiveData()
    var liveData : LiveData<ArrayList<SearchModel>> = mutableLiveData
    var liveUserList : LiveData<ArrayList<SearchUserModel>> = mutableUserList
    val searchstring : MutableLiveData<String> = MutableLiveData()
    var livestring : LiveData<String> = searchstring
    val searchRepository = SearchRepository()


    fun setString(string: String){
        searchstring.value = string
    }

    fun getUsersByUsername(){
        mutableUserList.postValue(searchRepository.getUserNameFromFirestore(searchstring.value.toString()))
    }

    fun getGroupsByTags(){
        val arrayList = searchRepository.getTagsFromFirestore(searchstring.value.toString())
        mutableLiveData.postValue(arrayList)
    }

    fun getGroupsByNames(){
        mutableLiveData.postValue(searchRepository.getNamesFromFirestore(searchstring.value.toString()))
    }

    fun getGroupsByGroupTag (){
        mutableLiveData.postValue(searchRepository.getGroupTagFromFirestore(searchstring.value.toString()))
    }
}