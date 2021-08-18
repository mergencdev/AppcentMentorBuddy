package com.mergenc.appcentmentorbudy.model

import java.util.*

data class GalleryImage(
    // java.lang.ClassCastException: com.google.firebase.Timestamp cannot be cast to java.util.Date

    /*val date: Date,*/
    val description: String,
    val imageURL: String,
    val title: String
)