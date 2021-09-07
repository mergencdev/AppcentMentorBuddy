package com.mergenc.appcentmentorbudy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import com.mergenc.appcentmentorbudy.model.TrashImage
import java.util.*
import kotlin.collections.ArrayList

class TrashViewModel : ViewModel() {
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