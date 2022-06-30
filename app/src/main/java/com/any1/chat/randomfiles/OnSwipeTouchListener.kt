package com.any1.chat.randomfiles

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

class OnSwipeTouchListener(ctx: Context?) : OnTouchListener {
    private val gestureDetector: GestureDetector
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }

    }
    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
    fun onSwipeRight() {}
    fun onSwipeLeft() {}
    fun onSwipeTop() {}
    fun onSwipeBottom() {}

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }
}

//open class OnSwipeTouchListener(context: Context) : OnTouchListener {
//    /**
//     * Gets the gesture detector.
//     *
//     * @return the gesture detector
//     */
//    val gestureDetector: GestureDetector
//
//    /* (non-Javadoc)
// * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
// */
//    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
//        return gestureDetector.onTouchEvent(motionEvent)
//    }
//
//    private inner class GestureListener : SimpleOnGestureListener() {
//        /* (non-Javadoc)
//     * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
//     */
//        override fun onDown(e: MotionEvent): Boolean {
//            return true
//        }
//
//        /* (non-Javadoc)
//     * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
//     */
//        override fun onFling(
//            e1: MotionEvent,
//            e2: MotionEvent,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            val result = false
//            try {
//                val diffY = e2.rawY - e1.rawY
//                val diffX = e2.rawX - e1.rawX
//                if (Math.abs(diffX) - Math.abs(diffY) > Companion.SWIPE_THRESHOLD) {
//                    if (Math.abs(diffX) > Companion.SWIPE_THRESHOLD && Math.abs(velocityX) > Companion.SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffX > 0) {
//                            onSwipeRight()
//                        } else {
//                            onSwipeLeft()
//                        }
//                    }
//                } else {
//                    if (Math.abs(diffY) > Companion.SWIPE_THRESHOLD && Math.abs(velocityY) > Companion.SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffY > 0) {
//                            onSwipeBottom()
//                        } else {
//                            onSwipeTop()
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//            }
//            return result
//        }
//
//    }
//    companion object {
//        private const val SWIPE_THRESHOLD = 100
//        private const val SWIPE_VELOCITY_THRESHOLD = 100
//    }
//    /**
//     * On swipe right.
//     */
//    fun onSwipeRight() {}
//
//    /**
//     * On swipe left.
//     */
//    open fun onSwipeLeft() {}
//
//    /**
//     * On swipe top.
//     */
//    fun onSwipeTop() {}
//
//    /**
//     * On swipe bottom.
//     */
//    fun onSwipeBottom() {}
//
//    /**
//     * Instantiates a new on swipe touch listener.
//     *
//     * @param context
//     * the context
//     */
//    init {
//        gestureDetector = GestureDetector(context, GestureListener())
//    }
//}
//


//class OnSwipeTouchListener(private val context: Context, function: () -> Unit) : OnTouchListener {
//    /**
//     * Gets the gesture detector.
//     *
//     * @return the gesture detector
//     */
//    val gestureDetector: GestureDetector
//
//    /* (non-Javadoc)
// * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
// */
//    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
//        return gestureDetector.onTouchEvent(motionEvent)
//    }
//
//    private inner class GestureListener : SimpleOnGestureListener() {
//        /* (non-Javadoc)
//     * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
//     */
//        override fun onDown(e: MotionEvent): Boolean {
//            return true
//        }
//
//        /* (non-Javadoc)
//     * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
//     */
//        override fun onFling(
//            e1: MotionEvent,
//            e2: MotionEvent,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            val result = false
//            try {
//                val diffY = e2.rawY - e1.rawY
//                val diffX = e2.rawX - e1.rawX
//                if (Math.abs(diffX) - Math.abs(diffY) > Companion.SWIPE_THRESHOLD) {
//                    if (Math.abs(diffX) > Companion.SWIPE_THRESHOLD && Math.abs(velocityX) > Companion.SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffX > 0) {
//                            onSwipeRight()
//                        } else {
//                            onSwipeLeft()
//                        }
//                    }
//                } else {
//                    if (Math.abs(diffY) > Companion.SWIPE_THRESHOLD && Math.abs(velocityY) > Companion.SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffY > 0) {
//                            onSwipeBottom()
//                        } else {
//                            onSwipeTop()
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//            }
//            return result
//        }
//
//    }
//    companion object {
//        private const val SWIPE_THRESHOLD = 100
//        private const val SWIPE_VELOCITY_THRESHOLD = 100
//    }
//
//    /**
//     * On swipe right.
//     */
//    fun onSwipeRight() {}
//
//    /**
//     * On swipe left.
//     */
//    fun onSwipeLeft() {}
//
//    /**
//     * On swipe top.
//     */
//    fun onSwipeTop() {}
//
//    /**
//     * On swipe bottom.
//     */
//    fun onSwipeBottom() {}
//
//    /**
//     * Instantiates a new on swipe touch listener.
//     *
//     * @param context
//     * the context
//     */
//    init {
//        gestureDetector = GestureDetector(context, GestureListener())
//    }
//}