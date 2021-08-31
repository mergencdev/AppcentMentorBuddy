package com.mergenc.appcentmentorbudy.activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.mergenc.appcentmentorbudy.R
import com.mergenc.appcentmentorbudy.databinding.ActivityUploadBinding
import com.mergenc.appcentmentorbudy.model.LoadingDialog
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.activity_upload.imageView
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.loading_item.*
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding // upload binding;
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permLauncher: ActivityResultLauncher<String>

    var selectedImage: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelID = "com.mergenc.appcentmentorbudy.activity"
    private val notificationDescription = "Image uploaded successfully."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_upload)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher()

        imageView.clipToOutline = true

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    // Upload button onClick ("Upload");
    fun upload(view: View) {
        val loading = LoadingDialog(this)
        loading.startLoading()
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
            }

        })

        // Make "No feed available." text invisible.
        // Feed available now;
        //feedLinearLayout.visibility = View.INVISIBLE

        // Universal Unique ID;
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"


        val reference = storage.reference
        val imageReferance = reference.child("images").child(imageName)
        //Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show()


        if (selectedImage != null) {
            imageReferance.putFile(selectedImage!!).addOnSuccessListener {
                // Download url to firestore;
                val uploadedImageReference = storage.reference.child("images").child(imageName)
                uploadedImageReference.downloadUrl.addOnSuccessListener {
                    val downloadURL = it.toString() // Image URL;

                    val galleryMap = hashMapOf<String, Any>()
                    galleryMap.put("downloadURL", downloadURL)
                    galleryMap.put("title", binding.editTextTitle.text.toString())
                    galleryMap.put("description", binding.editTextTextDescription.text.toString())
                    galleryMap.put("date", Timestamp.now())

                    firestore.collection("Images").add(galleryMap).addOnSuccessListener {
                        Toast.makeText(this, "Image uploaded succesfully.", Toast.LENGTH_SHORT)
                            .show()
                        println("URI LOG: $selectedImage")

                        // Send local notification;
                        notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val intent = Intent(this, LauncherActivity::class.java)
                        val pendingIntent = PendingIntent.getActivity(
                            this,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationChannel = NotificationChannel(
                                channelID,
                                notificationDescription,
                                NotificationManager.IMPORTANCE_HIGH
                            )
                            notificationChannel.enableLights(true)
                            //notificationChannel.lightColor =
                            notificationChannel.enableVibration(false)
                            notificationManager.createNotificationChannel(notificationChannel)

                            builder = Notification.Builder(this, channelID)
                                .setContentTitle("Uploaded")
                                .setContentText(notificationDescription)
                                .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                                .setContentIntent(pendingIntent)
                        } else {
                            builder = Notification.Builder(this)
                                .setContentTitle("Uploaded")
                                .setContentText(notificationDescription)
                                .setLargeIcon(
                                    BitmapFactory.decodeResource(
                                        this.resources,
                                        R.drawable.appcent_mentorbuddy
                                    )
                                )
                                .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                                .setContentIntent(pendingIntent)
                        }

                        notificationManager.notify(42, builder.build())

                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Camera button onClick;
    fun camera(view: View) {
        val intentToCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentToCamera, 42)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 42) {
            if (data != null) {
                val bitmapFromCamera = data.extras?.get("data") as Bitmap
                imageView.setImageBitmap(bitmapFromCamera)
            }
        }
    }

    // Gallery button onClick ("Select Image");
    fun uploadImage(view: View) {
        // if the permission is not granted;
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(view, "Permission needed for gallery.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give permission") {
                        // request permission;
                        permLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

                    }.show()
            } else {
                // request permission again;
                permLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            // if the permission is already granted;
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // startActivityForResult;
            activityResultLauncher.launch(intentToGallery)
        }
    }
    // I'm getting permission to access the gallery and get image from gallery

    private fun registerLauncher() {
        activityResultLauncher =
                // StartActivityForResult function in registerForActivityResult;
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // if image selected;
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedImage = intentFromResult.data
                        selectedImage?.let {
                            binding.imageView.setImageURI(it)

                            // for image round;
                            // src: https://stackoverflow.com/questions/2459916/how-to-make-an-imageview-with-rounded-corners
                            imageView.clipToOutline = true
                            // if image selected, make invisible "Select image" text;
                            textViewSelectImage.visibility = View.INVISIBLE
                        }
                    }
                }
            }

        permLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    // permission granted;
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                } else {
                    // permission denied;
                    Toast.makeText(this@UploadActivity, "Permission denied.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}