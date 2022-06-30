package com.any1.chat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.any1.chat.fragments.directmessage
import com.any1.chat.fragments.home
import com.any1.chat.fragments.testfrag


class ChatFragmentPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return testfrag()
            1 -> return directmessage()

            else -> {
                return home()
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
