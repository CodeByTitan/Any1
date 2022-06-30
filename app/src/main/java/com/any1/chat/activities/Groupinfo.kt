package com.any1.chat.activities

import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.any1.chat.R
import com.any1.chat.adapters.MembersAdapter
import com.any1.chat.adapters.RequestAdapter
import com.any1.chat.adapters.SearchTagsAdapter
import com.any1.chat.interfaces.*
import com.any1.chat.models.MemberModel
import com.any1.chat.models.RequestModel
import com.any1.chat.viewmodels.GroupInfoViewModel
import com.any1.chat.viewmodels.MembersViewModel
import com.bumptech.glide.Glide
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mikhaellopez.circularimageview.CircularImageView
import java.util.*


class Groupinfo : AppCompatActivity(), OnMemberClickListener,OnMenuClickListener,OnConnectClickListener, RequestRemovedListener{
    private lateinit var toolbar: Toolbar
    private lateinit var groupphoto: CircularImageView
    private lateinit var gcname :  EditText
    private lateinit var edittags: ImageView
    private lateinit var tagrecyclerview: RecyclerView
    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var requestAdapter : RequestAdapter
    private lateinit var mutemessages: SwitchMaterial
    private lateinit var mutecalls: SwitchMaterial
    private lateinit var nosimping: SwitchMaterial
    private lateinit var videocall: SwitchMaterial
    private lateinit var approval: SwitchMaterial
    private lateinit var rankupdates: CheckBox
    private lateinit var tagadapter: SearchTagsAdapter
    private lateinit var memberAdapter: MembersAdapter
    private lateinit var addmembers: TextView
    private lateinit var tagPreferences : SharedPreferences
    private lateinit var addmemberimageview: ImageView
    private var requestList = ArrayList<String>()
    private lateinit var requestsText : TextView
    private lateinit var addtogroup : Button
    private var imageurl = ""
    private lateinit var gctag : String
    private var memberList = ArrayList<String>()
    private lateinit var leavechat: TextView
    var groupname = ""
    private lateinit var membersViewModel: MembersViewModel
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var groupInfoViewModel : GroupInfoViewModel
    private var nosimpingbool : Boolean = false
    private var videocallbool : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val themepreferences = getSharedPreferences(packageName+"theme", MODE_PRIVATE)
        if(themepreferences.getString("theme","")=="dark"){
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES;
        }else{
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        }
        super.onCreate(savedInstanceState)
        tagPreferences = getSharedPreferences(packageName+"tag", MODE_PRIVATE)
        if(intent!=null && intent.getStringExtra("chat")=="chat"){
            tagPreferences.edit().clear().apply()
        }
        setContentView(R.layout.activity_groupinfo)
        gcname = findViewById(R.id.groupname)
        edittags = findViewById(R.id.edittags)
        val changephoto = findViewById<TextView>(R.id.changegroupphoto)
        tagrecyclerview = findViewById(R.id.tagrecyclerview)
        memberRecyclerView= findViewById(R.id.memberrecyclerview)
        mutecalls = findViewById(R.id.mutecall)
        mutemessages = findViewById(R.id.mutemessages)
        nosimping = findViewById(R.id.nosimping)
        approval = findViewById(R.id.approval)
        val tags = findViewById<TextView>(R.id.tags)
        val lock = findViewById<ImageView>(R.id.lock)
        var originalGroupName = intent.getStringExtra("gcname").toString()
        rankupdates = findViewById(R.id.rankingupdates)
        addmemberimageview = findViewById(R.id.addbutton)
        addmembers = findViewById(R.id.addmember)
        requestsText = findViewById<TextView>(R.id.requeststext)
        addtogroup = findViewById<Button>(R.id.addtogc)
        leavechat = findViewById(R.id.leavegroup)
        requestsRecyclerView = findViewById(R.id.requests)
        var hidetags = false
        var namelock = true
        requestsRecyclerView.layoutManager = LinearLayoutManager(this)
        videocall = findViewById(R.id.videocallenable)
        val accessibility = findViewById<AppCompatSpinner>(R.id.accessibility)
        toolbar = findViewById(R.id.gcinfotoolbar)
        toolbar.setNavigationOnClickListener { slideleft() }
        val edittagssettings = findViewById<ImageView>(R.id.edittagssettings)
        val accessibilitytext = findViewById<TextView>(R.id.textView16)
        val go = findViewById<ImageView>(R.id.go)
        val membercount = findViewById<TextView>(R.id.membercount)
        var isAdmin = false
        groupphoto = findViewById(R.id.gcphoto)
        gctag = intent.getStringExtra("tag").toString()
        go.visibility = View.GONE
        membersViewModel = ViewModelProvider(this).get(MembersViewModel::class.java)
        groupInfoViewModel = ViewModelProvider(this).get(GroupInfoViewModel::class.java)
        requestAdapter = RequestAdapter(this, object : OnRequestClickListener {
            override fun onRequestClicked(position: Int, requestList: ArrayList<RequestModel>) {
                val intent = Intent(this@Groupinfo,ViewProfile::class.java)
                intent.putExtra("name",requestList[position].name)
                intent.putExtra("username",requestList[position].username)
                intent.putExtra("uri", requestList[position].uri)
                val bndlAnimation = ActivityOptions.makeCustomAnimation(
                    this@Groupinfo,
                    R.anim.animenterslow,
                    R.anim.animexitslow
                ).toBundle()
                startActivity(intent,bndlAnimation)
            }
        },gctag,this)
        groupInfoViewModel.getGroupInfo(gctag)
        groupInfoViewModel.liveGroupInfoModelList.observe(this){
            val adminList = it.adminList
            for (i in adminList){
                if(i == auth.currentUser!!.uid){
                    isAdmin = true
                }
            }
            if(it.membercount.toString()== "1"){
                membercount.text = "${it.membercount} member"
            }else{
                membercount.text = "${it.membercount} members"
            }
            groupname = it.name
            memberList = it.membersList
            namelock = it.namelock
            imageurl = it.imageurl
            if(it.imageurl!="" && it.imageurl!="male" && it.imageurl!="female")Glide.with(applicationContext).load(it.imageurl).circleCrop().into(groupphoto)
            else groupphoto.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.wojakgc))
            if(isAdmin){
                lock.visibility = View.VISIBLE
                accessibility.visibility = View.VISIBLE
                accessibilitytext.visibility = View.VISIBLE
                videocall.visibility = View.VISIBLE
                approval.visibility = View.VISIBLE
                if(!namelock){
                    lock.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_cil_lock_unlocked))
                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setContentView(R.layout.namelockdialog)
                    dialog.findViewById<TextView>(R.id.ok).setOnClickListener{dialog.dismiss()}
                    dialog.show()
                }else{
                    lock.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_cil_lock_locked))
                }
                nosimping.isChecked = it.nosimping
                videocall.isChecked = it.videocall
                rankupdates.isChecked = it.rankupdates
                gcname.setText(it.name)
                originalGroupName = it.name
                if(it.accessibility.lowercase() == "public"){
                    accessibility.setSelection(0)
                }else{
                    accessibility.setSelection(1)
                }
                approval.isChecked = it.approval
            }else{
                gcname.isEnabled = !namelock
                accessibility.visibility = View.GONE
                accessibilitytext.visibility = View.GONE
                videocall.visibility = View.GONE
                approval.visibility = View.GONE
                rankupdates.visibility = View.GONE
                nosimping.visibility = View.GONE
                requestsText.visibility = View.GONE
                requestsRecyclerView.visibility = View.GONE
                addtogroup.visibility = View.GONE
                lock.visibility = View.GONE
            }

            if (it.hidetags && !isAdmin) {
                tags.visibility = View.GONE
                tagrecyclerview.visibility = View.GONE
                requestsText.visibility = View.GONE
                addtogroup.visibility = View.GONE
            } else {
                tagadapter = SearchTagsAdapter(this, isAdmin,gctag)
                tagrecyclerview.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                tagadapter.setTags(it.searchTags)
                when (it.searchTags.size) {
                    1 -> {
                        tagPreferences.edit().putString("primarytag", it.searchTags[0]).apply()
                        tagPreferences.edit().putString("counts", "1").apply()
                    }
                    2 -> {
                        tagPreferences.edit().putString("primarytag", it.searchTags[0]).apply()
                        tagPreferences.edit().putString("secondarytag", it.searchTags[1])
                            .apply()
                        tagPreferences.edit().putString("counts", "2").apply()
                    }
                    3 -> {
                        tagPreferences.edit().putString("primarytag", it.searchTags[0]).apply()
                        tagPreferences.edit().putString("secondarytag", it.searchTags[1])
                            .apply()
                        tagPreferences.edit().putString("tertiarytag", it.searchTags[2])
                            .apply()
                        tagPreferences.edit().putString("counts", "3").apply()
                    }
                }
                tagrecyclerview.adapter = tagadapter
            }
        }


        val accessibilityOptions = arrayListOf("Public", "Private")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.dropdown, accessibilityOptions)
        accessibility.adapter = adapter
        approval.setOnCheckedChangeListener { button, bool ->
            firestore.collection("groups").document(gctag).update(
                "approval", bool
            )
        }


        edittagssettings.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.edittagssettings)
            val hide = dialog.findViewById<TextView>(R.id.hide)
            val cancel = dialog.findViewById<TextView>(R.id.cancel)
            hide.setOnClickListener {
                firestore.collection("groups").document(gctag).update("hidetags", true)
                    .addOnSuccessListener { dialog.dismiss() }
            }
            cancel.setOnClickListener { dialog.dismiss() }
        }

        nosimping.setOnCheckedChangeListener { button, bool ->
            firestore.collection("groups").document(gctag).update("nosimping", bool)
        }

        accessibility.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    p1: View?,
                    i: Int,
                    p3: Long
                ) {
                    firestore.collection("groups").document(gctag)
                        .update("accessibility", accessibilityOptions[i])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        videocall.setOnCheckedChangeListener { button, bool ->
            firestore.collection("groups").document(gctag).update(
                "videocall", bool
            )
        }

        edittags.setOnClickListener {
            val intent = Intent(this, SearchTags::class.java)
            intent.putExtra("donotdelete", "donotdelete")
            intent.putExtra("gc", "gc")
            intent.putExtra("gctag",gctag)
            val bndlAnimation = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.anim_enter,
                R.anim.anim_exit
            ).toBundle()
            startActivity(intent, bndlAnimation)
        }

        rankupdates.setOnCheckedChangeListener { button, bool ->
            firestore.collection("groups").document(gctag).update("rankupdates", bool)
        }

        membersViewModel.getRequests(gctag)
        if(membersViewModel.liveRequestList.value == null){
            requestsText.visibility = View.GONE
            addtogroup.visibility = View.GONE
        }
        membersViewModel.liveRequestList.observe(this){
            if(it.size==0){
                requestsText.visibility = View.GONE
                addtogroup.visibility = View.GONE
            }else{
                requestsText.visibility = View.VISIBLE
                addtogroup.visibility = View.VISIBLE
                requestAdapter.setRequestList(it)
                requestsRecyclerView.adapter = requestAdapter
            }
        }

        var str = ""


        addtogroup.setOnClickListener {

        }

        gcname.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                private var timer = Timer()
                private val DELAY: Long = 1000 // Milliseconds
                override fun afterTextChanged(s: Editable) {
                    str = s.toString()
//                    Toast.makeText(this, str + "yoyo$originalGroupName", Toast.LENGTH_SHORT).show()
//            firestore.collection("groups").document(gctag).update("name",str)
                    if(str == originalGroupName) go.visibility = View.GONE
                    else go.visibility = View.VISIBLE
                }
            }
        )

        go.setOnClickListener {
            firestore.collection("groups").document(gctag).update("name",str).addOnSuccessListener {
                Toast.makeText(this, "Group name has been changed successfully", Toast.LENGTH_SHORT).show()
                go.visibility = View.GONE
            }
        }

        mutemessages.setOnCheckedChangeListener{
            button , bool ->
            firestore.collection("users").document(auth.currentUser!!.uid).collection("groups").document(gctag).update(
                "messagemuted", bool
            )
        }

        lock.setOnClickListener {
            if(namelock){
                namelock = false
                firestore.collection("groups").document(gctag).update("namelock",false).addOnSuccessListener {
                    lock.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_cil_lock_unlocked))
                }
            }else{
                namelock = true
                firestore.collection("groups").document(gctag).update("namelock",true).addOnSuccessListener {
                    lock.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ic_cil_lock_locked))
                }
            }
        }


        mutecalls.setOnCheckedChangeListener{
                button , bool ->
            firestore.collection("users").document(auth.currentUser!!.uid).collection("groups").document(gctag).update(
                "callmuted", bool
            )
        }


        memberRecyclerView.layoutManager = LinearLayoutManager(this)
        memberAdapter = MembersAdapter(this,this,this,this)
        membersViewModel.getMembers(gctag)
        membersViewModel.liveData.observe(this){
            memberAdapter.setMembersList(it)
            memberRecyclerView.adapter = memberAdapter
        }

    }

    override fun onStop() {
        groupInfoViewModel.stopListening()
        super.onStop()
    }

    override fun onPause() {
        groupInfoViewModel.stopListening()
        super.onPause()
    }

    override fun onNewIntent(intent: Intent?) {

        super.onNewIntent(intent)
    }

    override fun onBackPressed() {
        slideleft()
    }

    private fun slideleft(){
        tagPreferences.edit().clear().apply()
        val intent = Intent(this, Chat::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("gctag",gctag)
        intent.putExtra("imageurl",imageurl)
        intent.putExtra("gcname",groupname)
//        val bndlAnimation = ActivityOptions.makeCustomAnimation(
//            applicationContext,
//        ).toBundle()
        startActivity(intent)
        overridePendingTransition(
            R.anim.slideleft2,
            R.anim.slideleft1
        )
    }

    override fun onConnectClicked(position: Int, membersArrayList: ArrayList<MemberModel>) {

    }

    override fun onGroupMemberClicked(position: Int, memberArrayList: ArrayList<MemberModel>) {
        val intent = Intent(this,ViewProfile::class.java)
        intent.putExtra("groupinfo", "groupinfo")
        intent.putExtra("memberusername",memberArrayList[position].memberusername)
        val bndlAnimation = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.anim_enter,
            R.anim.anim_exit
        ).toBundle()
        startActivity(intent,bndlAnimation)
    }

    override fun onMenuClicked(position: Int, memberModeList: ArrayList<MemberModel>) {
        TODO("Not yet implemented")
    }

    override fun onRequestRemoved() {
        requestsText.visibility = View.GONE
        addtogroup.visibility = View.GONE
        requestsRecyclerView.visibility = View.GONE
    }
}
