package com.mergenc.appcentmentorbudy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.mergenc.appcentmentorbudy.model.GalleryImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor() : ViewModel() {
    val images = MutableLiveData<ArrayList<GalleryImage>>()

    fun receiveData(galleryImageArrayList: ArrayList<GalleryImage>, value: QuerySnapshot) {
        // Firebase Firestore;
        val documents = value.documents

        // Clear arraylist to avoid image duplications;
        galleryImageArrayList.clear()
        //tempGalleryImageArrayList.clear()

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
    }
}