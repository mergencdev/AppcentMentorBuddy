package com.mergenc.appcentmentorbudy.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mergenc.appcentmentorbudy.activity.UploadActivity
import com.mergenc.appcentmentorbudy.databinding.FragmentFeedBinding
import com.mergenc.appcentmentorbudy.model.GalleryImage
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*
import kotlin.collections.ArrayList

class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var galleryImageArrayList: ArrayList<GalleryImage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        galleryImageArrayList = ArrayList<GalleryImage>()

        getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_feed, container, false)

        binding = FragmentFeedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // open UploadActivity inside of FeedFragment
        floatingActionButton.setOnClickListener {
            val intent = Intent(activity, UploadActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    private fun getData() {
        db.collection("Images").addSnapshotListener { value, error ->

            if (error != null) {
                Toast.makeText(requireActivity(), error.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {

                        val documents = value.documents

                        for (document in documents) {
                            //val date = document.get("date") as Date // Error on this line;
                            val description = document.get("description") as String
                            val imageURL = document.get("downloadURL") as String
                            val title = document.get("title") as String

                            println(description)

                            val galleryImage = GalleryImage(/*date,*/ description, imageURL, title)
                            galleryImageArrayList.add(galleryImage)
                        }
                    }
                }
            }
        }
    }

}