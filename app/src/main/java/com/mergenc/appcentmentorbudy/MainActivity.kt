package com.mergenc.appcentmentorbudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mergenc.appcentmentorbudy.adapter.ViewPagerAdapter
import com.mergenc.appcentmentorbudy.fragment.FeedFragment
import com.mergenc.appcentmentorbudy.fragment.TrashFragment
import kotlinx.android.synthetic.main.activity_main.*

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