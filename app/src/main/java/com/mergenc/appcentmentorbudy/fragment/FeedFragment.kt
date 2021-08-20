package com.mergenc.appcentmentorbudy.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Adapter
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import com.ortiz.touchview.TouchImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detailed_image.*
import kotlinx.android.synthetic.main.detailed_image.view.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_trash.*
import kotlinx.android.synthetic.main.images_row.*
import java.io.ByteArrayOutputStream
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

        // Share image via Picasso;
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // open UploadActivity inside of FeedFragment
        floatingActionButton.setOnClickListener {
            val intent = Intent(activity, UploadActivity::class.java)
            activity?.startActivity(intent)
        }

        // resize images (FAB onClick);
        var count = 0
        fabResize.setOnClickListener {
            if (count % 2 == 0) {
                titleCardView.visibility = View.GONE
                descriptionCardView.visibility = View.GONE

                count++
            } else {
                titleCardView.visibility = View.VISIBLE
                descriptionCardView.visibility = View.VISIBLE
                count = 0
            }
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
                val mDialogView =
                    Dialog(requireContext(), android.R.style.Theme_Material_NoActionBar)
                mDialogView.setContentView(R.layout.detailed_image)

                Picasso.get().load(tempGalleryImageArrayList[position].imageURL).resize(1000, 1000)
                    .into(mDialogView.imageViewDetailed)

                mDialogView.show()

                // imageView onClick;
                var count = 1
                mDialogView.imageViewDetailed.setOnClickListener {
                    if (count % 2 != 0) {
                        mDialogView.cardViewDeleteShare.visibility = View.GONE
                        count += 1
                    } else {
                        mDialogView.cardViewDeleteShare.visibility = View.VISIBLE
                        count = 1
                    }
                }

                // Share button onClick;
                mDialogView.buttonShare.setOnClickListener {
                    val image: Bitmap? = getBitmapFromView(mDialogView.imageViewDetailed)

                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "image/*"
                    shareIntent.putExtra(
                        Intent.EXTRA_STREAM,
                        getImageUri(requireContext(), image!!)
                    )
                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                }

                // Trash button onClick - sends the selected item to the Trash;
                //mDialogView.buttonTrash.setOnClickListener {

                //}
            }
        })
    }

    // For share imageView;
    private fun getBitmapFromView(view: TouchImageView): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // For share imageView;
    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
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