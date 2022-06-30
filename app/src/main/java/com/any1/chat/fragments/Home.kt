package com.any1.chat.fragments

import android.R.attr.height
import android.app.ActivityOptions
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.any1.chat.MyCustomAnimation
import com.any1.chat.R
import com.any1.chat.activities.Chat
import com.any1.chat.activities.CreateGroup
import com.any1.chat.activities.Search
import com.any1.chat.adapters.GroupAdapter
import com.any1.chat.interfaces.OnGroupClickListener
import com.any1.chat.models.GroupModel
import com.any1.chat.randomfiles.CustomRecycleriew
import com.any1.chat.viewmodels.GroupViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home.newInstance] factory method to
 * create an instance of this fragment.
 */
class home : Fragment(),OnGroupClickListener{
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: GroupAdapter
    private lateinit var recyclerView: CustomRecycleriew
    private lateinit var viewModel: GroupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun slideUp(view: View) {
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            0F
        ) // toYDelta
        animate.duration = 500
//        animate.fillAfter = true
        view.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                view.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
    }

    // slide the view from its current position to below itself
    private fun slideDown(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            0F,  // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        animate.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                slideUp(view)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
    }

    private fun runAnimation(view: View){
        if (view.switchcard.visibility == View.VISIBLE) {
            val a = MyCustomAnimation(view.switchcard, 1000, MyCustomAnimation.COLLAPSE)
            a.height = view.height
            view.switchcard.startAnimation(a)
        } else {
            val a = MyCustomAnimation(view.switchcard, 1000, MyCustomAnimation.EXPAND)
            a.height = height
            view.switchimg.startAnimation(a)
            a.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    runAnimation(view)
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            })
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        val gctag = requireArguments().getString("gctag")
        val directmessage = view.findViewById<ImageView>(R.id.directmessage)
        val createGroup = view.findViewById<ImageView>(R.id.creategroup)
//        view.switchcard.alpha = 0f
//        view.switchcard.bringToFront()
        view.switchcard.visibility = View.INVISIBLE
//        if(requireActivity().intent.getStringExtra("switch")=="switch"){
////            view.switchcard.visibility = View.VISIBLE
////            view.switchcard.bringToFront()
//            view.switchtext.text = "Switched to $username"
//            if(imageurl!="" && imageurl!="male" && imageurl!="female"){
//                Glide.with(requireContext()).load(imageurl).circleCrop().into(view.switchimg)
//            }else{
//                if(imageurl=="male"){
//                     view.switchimg.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.gigachad))
//                }else{
//                    view.switchimg.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.doomergirl))
//                }
//            }

//            view.switchcard.animate().translationY(200f).alpha(1f).setDuration(800).start()
//            Handler(Looper.getMainLooper()).postDelayed({view.switchcard.animate().translationY(0f).alpha(0f).setDuration(800).start()},2000)
//
//            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_top_in)
//            val animation2 = AnimationUtils.loadAnimation(context, R.anim.slide_top_out)
//            view.switchcard.startAnimation(animation);
//            animation.setAnimationListener(object : Animation.AnimationListener{
//                override fun onAnimationStart(p0: Animation?) {
//                }
//
//                override fun onAnimationEnd(p0: Animation?) {
//                    view.switchcard.startAnimation(animation2)
////                    view.visibility = View.INVISIBLE
//                }
//
//                override fun onAnimationRepeat(p0: Animation?) {
//                }
//
//            })
//        }
        recyclerView = view.findViewById(R.id.gcrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = GroupAdapter(this,requireContext())
        viewModel = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)
        viewModel.getGroups.observe(viewLifecycleOwner
        ) {
            adapter.setGroupModelList(it)
            recyclerView.adapter = adapter
        }

        viewModel.liveuserid.observe(viewLifecycleOwner){
            viewModel.getGroups()
        }
//        ViewCompat.setNestedScrollingEnabled(recyclerView,true)
        createGroup.setOnClickListener {
            val intent = Intent(requireActivity(), CreateGroup::class.java)
            intent.putExtra("home","home")
            startActivity(intent)
        }
        val sharedPreferences2 = requireActivity().getSharedPreferences(requireContext().packageName+"theme", MODE_PRIVATE
        )
        val search = view.findViewById<ImageView>(R.id.search)
        search.setOnClickListener {
            val intent = Intent(requireActivity(), Search::class.java)
            val bndlAnimation = ActivityOptions.makeCustomAnimation(
                context,
                R.anim.anim_enter,
                R.anim.anim_exit
            ).toBundle()
            startActivity(intent,bndlAnimation)
        }
        val vp = requireActivity().findViewById<View>(R.id.viewpager) as ViewPager2
        vp.isUserInputEnabled = true

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        directmessage.setOnClickListener {
            vp.setCurrentItem(1,true)
        }

        if(sharedPreferences2.getString("theme","")=="dark"){
            directmessage.setColorFilter(Color.argb(255, 255, 255, 255));
        }else{
            directmessage.setColorFilter(Color.argb(0, 0, 0, 0));
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun OnGroupClicked(position: Int, groupModelList: ArrayList<GroupModel>) {
        val intent = Intent(requireActivity(), Chat::class.java)
        val groupModel = groupModelList[position]
        intent.putExtra("imageurl",groupModel.uri)
        intent.putExtra("gcname",groupModel.item)
        intent.putExtra("gctag",groupModel.tag)
        val bndlAnimation = ActivityOptions.makeCustomAnimation(
            context,
            R.anim.anim_enter,
            R.anim.anim_exit
        ).toBundle()
        startActivity(intent,bndlAnimation)
        requireActivity().overridePendingTransition(
            R.anim.anim_enter,
            R.anim.anim_exit
        )
    }
}