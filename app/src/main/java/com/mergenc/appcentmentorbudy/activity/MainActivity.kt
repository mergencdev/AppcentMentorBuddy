package com.mergenc.appcentmentorbudy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.mergenc.appcentmentorbudy.R
import com.mergenc.appcentmentorbudy.adapter.ViewPagerAdapter
import com.mergenc.appcentmentorbudy.fragment.FeedFragment
import com.mergenc.appcentmentorbudy.fragment.TrashFragment
import com.mergenc.appcentmentorbudy.fragment.UploadFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_feed.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTabs()
    }

    private fun setTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(FeedFragment(), "Feed")
        adapter.addFragment(TrashFragment(), "Trash")

        viewPager.adapter = adapter

        // give tab settings with viewpager;
        tabs.setupWithViewPager(viewPager)
        // give tab icon with setIcon by its index;
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_dynamic_feed_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_delete_24)
    }
}