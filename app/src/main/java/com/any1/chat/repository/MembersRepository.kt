package com.any1.chat.repository

import android.net.Uri
import androidx.core.net.toUri
import com.any1.chat.interfaces.GroupListListener
import com.any1.chat.interfaces.MemberListListener
import com.any1.chat.interfaces.RequestListListener
import com.any1.chat.interfaces.TagListListener
import com.any1.chat.models.MemberModel
import com.any1.chat.models.RequestModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.Member

class MembersRepository(val memberListListener : MemberListListener, val requestListListener: RequestListListener, val tagListListener: TagListListener) {
    private val auth = FirebaseAuth.getInstance()
    private var membersArrayList = ArrayList<MemberModel>()
    private var membersList = ArrayList<String>()
    private var adminList = ArrayList<String>()
    private val firestore = FirebaseFirestore.getInstance()
    private var isAdmin = false
    private var isConnected = false
    private lateinit var membername : String
    private lateinit var username : String
    private lateinit var uri : String
    private var tagList = ArrayList<String>()
    private lateinit var model : MemberModel
    private var requestList = ArrayList<RequestModel>()
    fun getMemberData(tag : String){
        firestore.collection("groups").document(tag).addSnapshotListener{
            value,error->
            if (value != null) {
                membersArrayList.clear()
                adminList.clear()
                if(value.get("members")!=null && value.get("admins")!=null){
                    membersList.addAll(value.get("members") as ArrayList<String>)
                    adminList.addAll(value.get("admins") as ArrayList<String>)
                    for(i in membersList){
                        firestore.collection("users").document(i).get().addOnSuccessListener {
                            membername = it.getString("displayname").toString()
                            username = it.getString("username").toString()
                            uri = it.getString("imageurl").toString()
                            firestore.collection("users").document(auth.currentUser!!.uid).collection("connections").document(i).get().addOnSuccessListener {
                                userdoc ->
                                isConnected = userdoc.exists()
                            }.addOnFailureListener{isConnected = false}
                            isAdmin = adminList.contains(i)
                            model = MemberModel(membername,username,uri,isConnected,isAdmin)
                            membersArrayList.add(model)
                            val filteredList = membersArrayList.distinct()
                            val filteredArrayList = ArrayList(filteredList)
                            memberListListener.showMemberList(filteredArrayList)
                        }
                    }
                }
            }
        }
    }

    fun getGroupTags(tag: String){
        firestore.collection("groups").document(tag).addSnapshotListener{
            value, error ->
            if(value!=null){
                tagList.clear()
                if(value.get("searchtags")!= null){
                    tagList = value.get("searchtags") as ArrayList<String>
                    tagListListener.showTagList(tagList)
                }
            }
        }
    }

    fun getRequests(tag : String){
        firestore.collection("groups").document(tag).addSnapshotListener {
            document, error ->
            requestList.clear()
            if(document!=null) {
                for(id in document.get("requests") as ArrayList<String>){
                    firestore.collection("users").document(id).get().addOnSuccessListener { doc ->
                        val name = doc.getString("displayname").toString()
                        val username = doc.getString("username").toString()
                        val uri = doc.getString("imageurl").toString()
                        val model = RequestModel(name, username, uri, true,id)
                        requestList.add(model)
                        requestListListener.showRequestList(requestList)
                    }
                }

            }
        }
    }
}