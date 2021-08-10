package com.mergenc.appcentmentorbudy.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.mergenc.appcentmentorbudy.R
import com.mergenc.appcentmentorbudy.activity.UploadActivity
import com.mergenc.appcentmentorbudy.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // open UploadActivity inside of FeedFragment
        floatingActionButton.setOnClickListener {
            val intent = Intent(activity, UploadActivity::class.java)
            activity?.startActivity(intent)
        }
    }

}