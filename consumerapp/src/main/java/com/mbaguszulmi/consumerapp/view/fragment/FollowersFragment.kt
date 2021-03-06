package com.mbaguszulmi.consumerapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mbaguszulmi.consumerapp.R
import com.mbaguszulmi.consumerapp.model.GithubUser
import com.mbaguszulmi.consumerapp.view.activity.DetailActivity
import com.mbaguszulmi.consumerapp.view.adapter.GithubUserListAdapter
import com.mbaguszulmi.consumerapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_followers.*


private const val ARG_USERNAME = "__arg_username__"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {
    private var username: String? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var activity: AppCompatActivity
    private lateinit var githubUserListAdapter: GithubUserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }

        if (getActivity() != null) {
            activity = getActivity() as AppCompatActivity
            mainViewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        initAdapter()

        mainViewModel.getFollowersLiveData().observe(activity, Observer {
            githubUserListAdapter.updateData(it)
        })
    }

    private fun initAdapter() {
        githubUserListAdapter = GithubUserListAdapter(activity, ArrayList())

        githubUserListAdapter.setOnItemClickListener(object: GithubUserListAdapter.OnItemClickListener {
            override fun onItemClick(user: GithubUser) {
                val moveIntent = Intent(activity, DetailActivity::class.java)
                moveIntent.putExtra(DetailActivity.EXTRA_GITHUB_USER, user)
                startActivity(moveIntent)
            }
        })

        rv_followers_list.layoutManager = LinearLayoutManager(activity)
        rv_followers_list.adapter = githubUserListAdapter
        rv_followers_list.isNestedScrollingEnabled = false
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param username Usernae of a github user.
         * @return A new instance of fragment FollowersFragment.
         */
        @JvmStatic
        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}