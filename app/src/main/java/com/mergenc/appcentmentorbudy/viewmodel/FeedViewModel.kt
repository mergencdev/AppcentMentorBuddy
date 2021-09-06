package com.mergenc.appcentmentorbudy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mergenc.appcentmentorbudy.model.GalleryImage

class FeedViewModel : ViewModel() {
    val images = MutableLiveData<ArrayList<GalleryImage>>()

    fun getData() {
        // Firebase Firestore;

    }
}