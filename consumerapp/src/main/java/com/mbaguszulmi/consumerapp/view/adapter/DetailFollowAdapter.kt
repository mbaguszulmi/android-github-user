package com.mbaguszulmi.consumerapp.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailFollowAdapter(private val context: Context, fa: FragmentActivity,
                            private val items: Array<Fragment>):
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }


}