package com.any1.chat.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TagsViewModel : ViewModel() {
    val primarytag = MutableLiveData<String>()
    val secondarytag = MutableLiveData<String>()
    val tertiarytag = MutableLiveData<String>()

    init {
        primarytag.value = ""
        secondarytag.value = ""
        tertiarytag.value = ""
    }
    fun updatePrimaryTag(s: String){
        primarytag.value = s
    }
    fun updateSecondaryTag(s: String){
        secondarytag.value = s
    }
    fun updateTertiaryTag(s: String){
        tertiarytag.value = s
    }

    fun getPrimaryTag(): String{
        return primarytag.value.toString()
    }
    fun getSecondaryTag():String{
        return secondarytag.value.toString()
    }
    fun getTertiaryTag(): String {
        return tertiarytag.value.toString()
    }
}