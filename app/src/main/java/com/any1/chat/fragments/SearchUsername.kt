//package com.any1.chat.fragments
//
//import android.app.ActivityOptions
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.any1.chat.R
//import com.any1.chat.activities.ViewProfile
//import com.any1.chat.adapters.SearchAdapter
//import com.any1.chat.adapters.UserAdapter
//import com.any1.chat.interfaces.OnSearchUserClickListener
//import com.any1.chat.models.UserModel
//import com.any1.chat.viewmodels.MembersViewModel
//import com.any1.chat.viewmodels.SearchViewModel
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [SearchUsername.newInstance] factory method to
// * create an instance of this fragment.
// */
//class SearchUsername : Fragment() , OnSearchUserClickListener{
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    private lateinit var viewModel: SearchViewModel
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter : UserAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_search_username, container, false)
//        recyclerView = view.findViewById(R.id.searchuserrecyclerview)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapter = UserAdapter(this,requireContext())
//        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
//        viewModel.getUsersByUsername()
//        viewModel.liveUserList.observe(viewLifecycleOwner){
//            adapter.setUserList(it)
//            recyclerView.adapter = adapter
//        }
//        return view
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SearchUsername.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SearchUsername().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//
//    override fun onUserClicked(position: Int, userList: ArrayList<UserModel>) {
//        val intent = Intent(requireActivity(),ViewProfile::class.java)
//        val bndlAnimation = ActivityOptions.makeCustomAnimation(
//            context,
//            R.anim.animenterslow,
//            R.anim.animexitslow
//        ).toBundle()
//        startActivity(intent,bndlAnimation)
//    }
//}