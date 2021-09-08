package com.mergenc.appcentmentorbudy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.mergenc.appcentmentorbudy.model.TrashImage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TrashViewModel @Inject constructor() : ViewModel() {
    val trashes = MutableLiveData<ArrayList<TrashImage>>()

    fun receiveTrash(trashArrayList: ArrayList<TrashImage>, value: QuerySnapshot) {
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
    }
}