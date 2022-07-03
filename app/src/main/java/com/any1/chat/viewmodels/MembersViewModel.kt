package com.any1.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.any1.chat.interfaces.MemberListListener
import com.any1.chat.interfaces.RequestListListener
import com.any1.chat.interfaces.TagListListener
import com.any1.chat.models.MemberModel
import com.any1.chat.models.RequestModel
import com.any1.chat.repository.MembersRepository

class MembersViewModel : ViewModel(), MemberListListener, RequestListListener, TagListListener{
    private val mutableLiveData = MutableLiveData<ArrayList<MemberModel>>()
    val liveData : LiveData<ArrayList<MemberModel>> = mutableLiveData
    private val requestList = MutableLiveData<List<RequestModel>>()
    val liveRequestList : LiveData<List<RequestModel>> = requestList
    private val mutableTagsList = MutableLiveData<ArrayList<String>>()
    val liveTagsList : LiveData<ArrayList<String>> = mutableTagsList
    private val membersRepository = MembersRepository(this,this,this)

    fun getMembers(string: String){
        membersRepository.getMemberData(string)
    }

    fun getRequests(string: String){
        membersRepository.getRequests(string)
    }

    fun stopListeningForGroupInfo(){
        membersRepository.removeListener()
    }

    fun getTags(string: String){
        membersRepository.getGroupTags(string)
    }

    override fun showMemberList(arrayList: ArrayList<MemberModel>) {
        mutableLiveData.postValue(arrayList)
    }

    override fun showRequestList(arrayList: List<RequestModel>?) {
       requestList.postValue(arrayList)
    }

    override fun showTagList(arrayList: ArrayList<String>) {
        mutableTagsList.postValue(arrayList)
    }
}