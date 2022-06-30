package com.any1.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.any1.chat.interfaces.GroupInfoChangeListener
import com.any1.chat.models.GroupInfoModel
import com.any1.chat.repository.GroupInfoRepository

class GroupInfoViewModel : ViewModel(), GroupInfoChangeListener{
    private val mutableGroupInfoModelList = MutableLiveData<GroupInfoModel>()
    val liveGroupInfoModelList : LiveData<GroupInfoModel> = mutableGroupInfoModelList
    private val groupInfoRepository = GroupInfoRepository(this)

    fun getGroupInfo(string: String){
       groupInfoRepository.getGroupInfo(string)
    }

    override fun onGroupInfoChanged(groupInfoModel: GroupInfoModel) {
        mutableGroupInfoModelList.postValue(groupInfoModel)
    }

    fun stopListening(){
        groupInfoRepository.removeListener()
    }

}