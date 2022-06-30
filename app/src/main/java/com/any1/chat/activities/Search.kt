package com.any1.chat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.any1.chat.R
import com.any1.chat.adapters.SearchUserAdapter
import com.any1.chat.adapters.ViewPagerFragmentAdapter
import com.any1.chat.models.SearchUserModel
import com.any1.chat.viewmodels.SearchViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class Search : AppCompatActivity() {
//    private lateinit var recyclerView: RecyclerView
    private lateinit var changesearch : ImageView
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchView: SearchView
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private lateinit var tablayout : TabLayout
    private lateinit var adapter : SearchUserAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewPagerFragmentAdapter: ViewPagerFragmentAdapter
    private val titles = arrayOf("tags","names","group tag")
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences2 = getSharedPreferences(packageName+"theme", MODE_PRIVATE)
        if(sharedPreferences2.getString("theme","")=="dark"){
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES;
        }else{
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchView = findViewById(R.id.searchsearchview)
        changesearch = findViewById(R.id.changesearch)
        viewPager2 = findViewById(R.id.searchviewpager)
        val recyclerView : RecyclerView = findViewById(R.id.searchuserrecyclerview)
        toolbar = findViewById(R.id.searchtoolbar)
        tablayout = findViewById(R.id.tablayout)
        adapter = SearchUserAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this)
        if(toolbar.title == "Search People"){
            recyclerView.visibility = View.VISIBLE
        }else{
            recyclerView.visibility = View.GONE
        }
        viewPager2.adapter = viewPagerFragmentAdapter
        TabLayoutMediator(
            tablayout, viewPager2
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        sharedPreferences = getSharedPreferences(packageName+"search", MODE_PRIVATE)
        viewModel.searchstring.observe(this){
            if(recyclerView.visibility == View.VISIBLE){
                viewModel.getUsersByUsername()
                if(it==""){
                    adapter.clearUsersList()
                }
            }
        }

        viewModel.liveUserList.observe(this){
            if(recyclerView.visibility == View.VISIBLE){
//                val temporaryList = ArrayList<SearchUserModel>()
//                temporaryList.clear()
//                for (i in it){
//                    if(!temporaryList.contains(i)){
//                        temporaryList.add(i)
//                    }
//                }
//                var i=0
//                while(i<it.size){
//                    i++
//                    if(i!=it.size) {
//                        if (it[i] == it[i + 1]){
//                            it.removeAt(i+1)
//                        }
//                    }
//                }

//                val set: Set<SearchUserModel> = HashSet(it)
//                it.clear()
//                it.addAll(set)
                adapter.setAdapterList(it)
                recyclerView.adapter = adapter
            }
        }
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
//                viewModel.setString(query)
                return false
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    viewModel.setString(newText)
                }
                return true
            }
        })

        toolbar.setNavigationOnClickListener { slideleft() }
        changesearch.setOnClickListener {
            if(sharedPreferences.getString("groups","")!="false"){
                sharedPreferences.edit().putString("groups","false").apply()
                toolbar.title = "Search People"
                recyclerView.visibility = View.VISIBLE
                viewPager2.visibility = View.INVISIBLE
                viewPager2.isEnabled = false
                tablayout.visibility = View.INVISIBLE
                tablayout.isEnabled = false
            }else{
                sharedPreferences.edit().putString("groups","true").apply()
                viewPager2.visibility = View.VISIBLE
                viewPager2.isEnabled = true
                toolbar.title = "Search Groups"
                recyclerView.visibility = View.GONE
                tablayout.visibility = View.VISIBLE
                tablayout.isEnabled = true
            }
        }

    }

    private fun slideleft(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val bndlAnimation = ActivityOptions.makeCustomAnimation(
//            applicationContext,
//        ).toBundle()
        startActivity(intent)
        overridePendingTransition(
            R.anim.slideleft2,
            R.anim.slideleft1
        )
    }
    override fun onBackPressed() {
       slideleft()
    }
}