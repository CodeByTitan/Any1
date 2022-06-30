package com.any1.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.any1.chat.models.SearchUserModel
import com.any1.chat.repository.SearchRepository

class SearchUserViewModel : ViewModel() {
    private val mutableLiveData = MutableLiveData<ArrayList<SearchUserModel>>()
    val liveData : LiveData<ArrayList<SearchUserModel>> = mutableLiveData
    val searchUserRepository = SearchRepository()
}