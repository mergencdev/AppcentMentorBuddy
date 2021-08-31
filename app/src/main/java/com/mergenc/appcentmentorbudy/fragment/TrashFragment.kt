package com.mergenc.appcentmentorbudy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mergenc.appcentmentorbudy.adapter.TrashRVAdapter
import com.mergenc.appcentmentorbudy.databinding.FragmentTrashBinding
import com.mergenc.appcentmentorbudy.model.TrashImage
import kotlinx.android.synthetic.main.fragment_trash.*
import java.util.*
import kotlin.collections.ArrayList

class TrashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var trashArrayList: ArrayList<TrashImage>

    private lateinit var trashRVAdapter: TrashRVAdapter
    private lateinit var binding: FragmentTrashBinding

    var trashCounter = 0

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
        //return inflater.inflate(R.layout.fragment_trash, container, false)

        setHasOptionsMenu(true)

        binding = FragmentTrashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTrashData()

        binding.trashRecyclerView.layoutManager =
            GridLayoutManager(requireActivity(), 2, RecyclerView.VERTICAL, false)

        val adapter = TrashRVAdapter(trashArrayList)
        trashRVAdapter = adapter
        binding.trashRecyclerView.adapter = trashRVAdapter
    }

    // Get trash data from collection: Trash from Firestore;
    private fun getTrashData() {
        db.collection("Trash").addSnapshotListener { value, error ->

            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        //println("valuesize: ${value.size()}")
                        clearTrash(value.size())

                        // Make "No feed available." text invisible;
                        // Feed available now;
                        trashLinearLayout.visibility = View.INVISIBLE // from fragment_trash.xml;

                        val documents = value.documents

                        trashArrayList.clear()

                        for (document in documents) {
                            val trashTitle = document.get("title") as String
                            val trashDescription = document.get("description") as String
                            val trashDownloadURL = document.get("downloadURL") as String
                            val trashDate = document.getDate("date") as Date

                            //println("TRASH: $trashDescription") // it's working;

                            val trashImage =
                                TrashImage(
                                    trashDescription,
                                    trashDownloadURL,
                                    trashTitle,
                                    trashDate
                                )

                            // Add all images to ArrayList;
                            trashArrayList.add(trashImage)
                        }

                        trashRVAdapter.notifyDataSetChanged()
                    } else {
                        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
                        // Make "The trash is empty." text visible;
                        // The trash is empty now;
                        trashLinearLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun clearTrash(valueSize: Int) {
        //trashCounter++
        println("TRASH SIZE: ${valueSize}")
        if (valueSize == 3) {
            db.collection("Trash").get().addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("Trash").document(document.id).delete()
                }
            }
        }

        requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
    }
}