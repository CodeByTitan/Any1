package com.any1.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.any1.chat.interfaces.GroupListListener
import com.any1.chat.models.GroupModel
import com.any1.chat.repository.GroupRepository
import com.any1.chat.repository.UserRepository


class GroupViewModel():ViewModel(), GroupListListener {
    private val mutableLiveData: MutableLiveData<ArrayList<GroupModel>> = MutableLiveData<ArrayList<GroupModel>>()
    val getGroups: LiveData<ArrayList<GroupModel>> = mutableLiveData
    var groupRepository = GroupRepository(this)
    private val userid = MutableLiveData<String>()
    val liveuserid : LiveData<String> = userid
    init {
        groupRepository.getGroups()
    }
    fun getGroups(){
        groupRepository.getGroups()
    }
    fun updateAuthId(string: String){
        userid.postValue(string)
    }
    override fun showListOfUser(groupModelList: ArrayList<GroupModel>) {
        mutableLiveData.value = groupModelList
    }
}