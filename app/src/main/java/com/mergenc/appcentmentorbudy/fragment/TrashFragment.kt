package com.mergenc.appcentmentorbudy.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
import com.mergenc.appcentmentorbudy.viewmodel.FeedViewModel
import com.mergenc.appcentmentorbudy.viewmodel.TrashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_trash.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TrashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var trashArrayList: ArrayList<TrashImage>

    private lateinit var trashRVAdapter: TrashRVAdapter
    private lateinit var binding: FragmentTrashBinding

    var trashCounter = 0

    // Field Injection for Trash;
    val trashViewModel: TrashViewModel by viewModels()

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

        observerLiveData()
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

                        trashArrayList.clear()
                        // Make "No feed available." text invisible;
                        // Feed available now;
                        trashLinearLayout.visibility = View.INVISIBLE // from fragment_trash.xml;

                        trashViewModel.receiveTrash(trashArrayList, value)

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

    fun observerLiveData() {
        trashViewModel.trashes.observe(viewLifecycleOwner, androidx.lifecycle.Observer { trashes ->
            trashes?.let {
                trashRVAdapter.updateTrashList(trashes)
            }
        })
    }
}