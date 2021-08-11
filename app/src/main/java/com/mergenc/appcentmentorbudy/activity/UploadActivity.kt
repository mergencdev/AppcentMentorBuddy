package com.mergenc.appcentmentorbudy.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mergenc.appcentmentorbudy.R
import com.mergenc.appcentmentorbudy.databinding.ActivityUploadBinding
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.jar.Manifest

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding // upload binding;
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permLauncher: ActivityResultLauncher<String>

    var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_upload)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher()
    }

    fun uploadImage(view: View) {
        // if the permission is not granted;
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //
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