package com.mbaguszulmi.githubuser.view.fragment

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
import com.mbaguszulmi.githubuser.R
import com.mbaguszulmi.githubuser.model.GithubUser
import com.mbaguszulmi.githubuser.view.activity.DetailActivity
import com.mbaguszulmi.githubuser.view.adapter.GithubUserListAdapter
import com.mbaguszulmi.githubuser.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_following.*

private const val ARG_USERNAME = "__arg_username__"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        initAdapter()

        mainViewModel.getFollowingLiveData().observe(activity, Observer {
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

        rv_following_list.layoutManager = LinearLayoutManager(activity)
        rv_following_list.adapter = githubUserListAdapter
        rv_following_list.isNestedScrollingEnabled = false
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param username Usernae of a github user.
         * @return A new instance of fragment FollowingFragment.
         */
        @JvmStatic
        fun newInstance(username: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }
}