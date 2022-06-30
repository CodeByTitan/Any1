package com.any1.chat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.any1.chat.fragments.searchbygctags
import com.any1.chat.fragments.searchbyname
import com.any1.chat.fragments.searchbyuniquetag

class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val titles = arrayOf("tags", "name", "group tag")
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> searchbygctags()
            1 -> searchbyname()
            2 -> searchbyuniquetag()
            else -> searchbygctags()
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}
