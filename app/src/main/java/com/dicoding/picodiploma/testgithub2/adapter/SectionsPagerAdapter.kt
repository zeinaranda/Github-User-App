package com.dicoding.picodiploma.testgithub2.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.picodiploma.testgithub2.ui.follow.FollowerFragment
import com.dicoding.picodiploma.testgithub2.ui.follow.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var login: String? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.newInstance(login.toString())
            1 -> fragment = FollowingFragment.newInstance(login.toString())
        }
        return fragment as Fragment
    }
}

