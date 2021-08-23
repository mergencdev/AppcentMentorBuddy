package com.mergenc.appcentmentorbudy.fragment

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
import com.mergenc.appcentmentorbudy.R
import com.mergenc.appcentmentorbudy.model.TrashImage

class TrashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var trashArrayList: ArrayList<TrashImage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        trashArrayList = ArrayList<TrashImage>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTrashData()
    }

    // Get trash data from collection: Trash from Firestore;
    private fun getTrashData() {
        db.collection("Trash").addSnapshotListener { value, error ->

            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        val documents = value.documents

                        for (document in documents) {
                            val trashTitle = document.get("title") as String
                            val trashDescription = document.get("description") as String
                            val trashDownloadURL = document.get("downloadURL") as String

                            println("TRASH: $trashDescription")

                            val trashImage =
                                TrashImage(trashDescription, trashDownloadURL, trashTitle)

                            // Add all images to ArrayList;
                            trashArrayList.add(trashImage)
                        }
                    }
                }
            }
        }
    }
}