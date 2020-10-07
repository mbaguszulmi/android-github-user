package com.mbaguszulmi.githubuser.view.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mbaguszulmi.githubuser.R

class DetailFollowAdapter(private val context: Context, fa: FragmentActivity,
                            private val items: Array<Fragment>):
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }


}