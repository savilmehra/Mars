package com.`in`.mars

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.`in`.mars.ui.ImageSelectorActivity
import com.`in`.mars.ui.RoomDatabase
import com.`in`.mars.ui.Url
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException


class FragmentUpload : Fragment() {

    private var storageRef: StorageReference? = null
    private var app: FirebaseApp? = null
    private var storage: FirebaseStorage? = null
    private var uri: Uri? = null
    val ALL_PERMISSIONS = 1236
    val PICK_IMAGE_REQUEST = 100
    private var btn_upload: Button? = null
    private var pb: ProgressBar? = null
    private var iv: ImageView? = null
    private var cam: ImageView? = null
    private var gall: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.ulpoad_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app = FirebaseApp.getInstance()
        storage = FirebaseStorage.getInstance(app!!)
        storageRef = FirebaseStorage.getInstance().reference.child("Randomdata")
        btn_upload = view.findViewById(R.id.btn_upload)
        pb = view.findViewById(R.id.pb)
        iv = view.findViewById(R.id.iv)
        cam = view.findViewById(R.id.cam)
        gall = view.findViewById(R.id.gal)
        gall!!.setOnClickListener {
            if (checkPermission()) {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST
                )
            }
        }

        cam!!.setOnClickListener {
            val intent = Intent(context, ImageSelectorActivity::class.java)
            startActivityForResult(intent, 102)
        }




        btn_upload!!.setOnClickListener {
            if (uri != null && !uri!!.equals("")) {
                uploadImage()
            }

        }
        var permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(context as Activity, permissions, ALL_PERMISSIONS)


    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            var permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(context as Activity, permissions, ALL_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            ALL_PERMISSIONS -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return
            }

        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()

    }

    companion object {
        fun newInstance(): FragmentUpload {
            val frg = FragmentUpload()
            return frg
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data as Uri
            if (selectedImage != null) {
                uri = selectedImage
                Glide.with(context!!)
                    .load(selectedImage)
                    .into(iv!!)


            }

        } else if (requestCode == 102) {
           uri= Uri.fromFile( File(data!!.getStringExtra("output_uri")))
            Glide.with(context!!)
                .load( uri)
                .into(iv!!)
        }


    }

    private fun uploadImage() {
        pb!!.visibility = View.VISIBLE
        val storageRefrence =
            storageRef!!.child("image" + uri!!.lastPathSegment)
        val uploadTask = storageRefrence.putFile(uri!!)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                pb!!.visibility = View.GONE
                task.exception?.let {
                    throw it
                    pb!!.visibility = View.GONE
                }
            }
            storageRefrence.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                pb!!.visibility = View.GONE
                val downloadUri = task.result
                val db = Url()
                db.url = downloadUri.toString()
                uri = null
                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_LONG).show()
                SaveDataToDb(context!!, db).execute()
            } else {
                Toast.makeText(context, "failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    inner class SaveDataToDb internal constructor(
        private val context: Context,
        private val UrlsDB: Url
    ) : AsyncTask<Void, Void, Void>() {
        private var db: RoomDatabase? = null

        init {
            if (context != null)
                db = RoomDatabase.getAppDatabase(context)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            if (db != null)
                db!!.daoUrls().insertNew(UrlsDB)
            return null
        }

    }


}