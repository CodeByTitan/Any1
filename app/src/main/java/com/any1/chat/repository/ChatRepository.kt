package com.any1.chat.repository

import android.util.Log
import android.widget.Toast
import com.any1.chat.activities.Chat
import com.any1.chat.interfaces.MessageReceiveListener
import com.any1.chat.models.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class ChatRepository ( val messageReceiveListener : MessageReceiveListener) {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val messageList: ArrayList<ArrayList<ChatModel>?> = ArrayList()
    private var listWithoutDotsStr = ArrayList<String>()
    private lateinit var sortedList : List<String>
    private lateinit var finalList : List<String>
    private val messageListForADay = ArrayList<ChatModel>()

    suspend fun getMessages(grouptag: String) {
        firestore.collection("groups").document(grouptag).collection("messages").orderBy("timestamp",Query.Direction.DESCENDING)
            .addSnapshotListener { value1, error ->
                if (value1 != null) {
                    messageList.clear()
                    for (i in value1.documents) {
                        messageListForADay.clear()
                        val doc = i.id
                        firestore.collection("groups").document(grouptag).collection("messages").document(doc).collection("messages").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener{
                            value,error ->
                            if(value!=null) {
                                for (document in value.documents) {
                                    val senderid = document.getString("sender").toString()
                                    val message = document.getString("message").toString()
                                    val senderpfpurl = document.getString("senderpfpuri").toString()
                                    val model = ChatModel(message, document.id, senderid, senderpfpurl)
                                    messageListForADay.add(model)
                                }
//                                if(i == value1.documents[0]){
//                                    if(messageList.size!=0){
//                                        messageList.removeAt(0)
//                                        messageList.add(messageListForADay)
//                                    }
//                                }

                            }
                        }
                        messageList.add(messageListForADay)
                        messageReceiveListener.OnMessageReceived(messageList)
                    }
                }
            }
    }
    private fun getDocumentsSortedByDate(dates : ArrayList<String>): List<String>{
        for (it in dates) {
            val dotRemovedString = it.replace(".","")
            listWithoutDotsStr.add(dotRemovedString)
        }
        listWithoutDotsStr.sortBy { it.substring(0,2) }
        listWithoutDotsStr.sortBy { it.substring(2,4) }
        listWithoutDotsStr.sortBy {it.substring(it.length-4,it.length)}
//        Log.d("list",listWithoutDotsStr.toString())
        val listwithdots = java.util.ArrayList<String>()
        for (i in listWithoutDotsStr){
            val stringBuilder = StringBuilder(i)
            stringBuilder.insert(2,".")
            stringBuilder.insert(i.length-3,".")
            listwithdots.add(stringBuilder.toString())
        }
        finalList = listwithdots.reversed()
//        Log.d("list",listwithdots.toString())
//        Log.d("list",finalList.toString())
        return finalList
    }
}
