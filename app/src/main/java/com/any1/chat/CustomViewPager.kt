package com.any1.chat

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2

class CustomViewPager(context: Context, attributeSet: AttributeSet): FrameLayout(context, attributeSet) {

    lateinit var vp: ViewPager2;

    var page: Int = 0;

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount == 1 && get(0) is ViewPager2) {
            vp = get(0) as ViewPager2
        } else {
            throw IllegalStateException();
        }

        vp.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                page = position;
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (page == 1 && vp.childCount > 0) {
            return vp.findViewWithTag<View>("second").dispatchTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }
}