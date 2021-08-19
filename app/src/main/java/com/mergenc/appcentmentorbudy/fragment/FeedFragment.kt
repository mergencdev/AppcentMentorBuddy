package com.mergenc.appcentmentorbudy.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Adapter
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mergenc.appcentmentorbudy.R
import com.mergenc.appcentmentorbudy.activity.UploadActivity
import com.mergenc.appcentmentorbudy.adapter.RecyclerViewAdapter
import com.mergenc.appcentmentorbudy.databinding.FragmentFeedBinding
import com.mergenc.appcentmentorbudy.model.GalleryImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detailed_image.*
import kotlinx.android.synthetic.main.detailed_image.view.*
import kotlinx.android.synthetic.main.details_dialog.*
import kotlinx.android.synthetic.main.details_dialog.view.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.images_row.*
import java.util.*
import kotlin.collections.ArrayList

class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var galleryImageArrayList: ArrayList<GalleryImage>
    private lateinit var tempGalleryImageArrayList: ArrayList<GalleryImage>

    private lateinit var imageAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        galleryImageArrayList = ArrayList<GalleryImage>()
        tempGalleryImageArrayList = ArrayList<GalleryImage>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_feed, container, false)

        setHasOptionsMenu(true)

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

        getData()

        binding.recyclerView.layoutManager =
            GridLayoutManager(requireActivity(), 2, RecyclerView.VERTICAL, false)

        val adapter = RecyclerViewAdapter(tempGalleryImageArrayList)
        imageAdapter = adapter
        binding.recyclerView.adapter = imageAdapter

        // RecyclerView item on click function here;
        adapter.setOnItemClickListener(object : RecyclerViewAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                // Inflate dialog;
                val mDialogView =Dialog(requireContext(), android.R.style.Theme_Material_NoActionBar)
                mDialogView.setContentView(R.layout.detailed_image)

                Picasso.get().load(tempGalleryImageArrayList[position].imageURL).resize(1000, 1000)
                    .into(mDialogView.imageViewDetailed)

                mDialogView.show()
            }
        })
    }

    private fun getData() {
        // orderBy("date", Query.Direction.ASCENDING) function can sort images by date;
        db.collection("Images").orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Toast.makeText(requireActivity(), error.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            // Make "No feed available." text invisible;
                            // Feed available now;
                            feedLinearLayout.visibility = View.INVISIBLE

                            val documents = value.documents

                            // Clear arraylist to avoid image duplications;
                            galleryImageArrayList.clear()
                            tempGalleryImageArrayList.clear()

                            for (document in documents) {
                                //val date = document.get("date") as Date // Error on this line;
                                val description = document.get("description") as String
                                val imageURL = document.get("downloadURL") as String
                                val title = document.get("title") as String

                                //println(description)

                                val galleryImage =
                                    GalleryImage(/*date,*/ description, imageURL, title)
                                galleryImageArrayList.add(galleryImage)
                            }

                            // Search;
                            tempGalleryImageArrayList.addAll(galleryImageArrayList)

                            // data updated, show new one;
                            imageAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)

        val item = menu.findItem(R.id.search_action)
        val searcView = item.actionView as SearchView
        searcView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            // When the text on the search bar changed, this fun run;
            override fun onQueryTextChange(newText: String?): Boolean {
                tempGalleryImageArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())

                if (searchText.isNotEmpty()) {
                    galleryImageArrayList.forEach {
                        if (it.title.lowercase(Locale.getDefault()).contains(searchText)) {
                            tempGalleryImageArrayList.add(it)
                        }
                    }

                    imageAdapter.notifyDataSetChanged()
                } else {
                    tempGalleryImageArrayList.clear()
                    tempGalleryImageArrayList.addAll(galleryImageArrayList)

                    imageAdapter.notifyDataSetChanged()
                }

                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

}