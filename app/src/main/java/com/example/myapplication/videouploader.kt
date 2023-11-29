package com.example.myapplication

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.example.myapplication.databinding.ActivityVideouploaderBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class videouploader : AppCompatActivity() {
    private lateinit var binding: ActivityVideouploaderBinding
    private lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        progressDialog= ProgressDialog(this)
        super.onCreate(savedInstanceState)
        binding = ActivityVideouploaderBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.videoView.isVisible = false
        binding.videouploader.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "video/*"
            videolauncher.launch(intent)
        }
    }

    val videolauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it->
        if(it.resultCode== Activity.RESULT_OK){
            if(it.data!=null){
                progressDialog.setTitle("Uploading Video...")
                progressDialog.show()
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()
                val ref= Firebase.storage.reference.child("Video/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data!!))
                ref.putFile(it.data!!.data!!).addOnSuccessListener{
                    ref.downloadUrl.addOnSuccessListener{

                        Firebase.database.reference.child("Video").push().setValue(it.toString())
                        progressDialog.dismiss()
//                        binding.imageView6.setImageURI(it)
//                        Picasso.get().load(it.toString()).into(binding.videoView);
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

                        binding.videouploader.isVisible=false
                        binding.videoView.isVisible = true
                        val mediaConroller=android.widget.MediaController(this)
                        mediaConroller.setAnchorView(binding.videoView)
                        binding.videoView.setVideoURI(it)
                        binding.videoView.setMediaController((mediaConroller))
                        binding.videoView.start()
                        binding.videoView.setOnCompletionListener {
                            ref.delete().addOnSuccessListener {
                                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                }
                    .addOnProgressListener {
                        val value=(it.bytesTransferred/it.totalByteCount)*100
                        progressDialog.setTitle("Uploaded "+value.toString()+"%")
                    }
            }
        }
    }

    private fun getFileType(data: Uri): String? {
        val mimetype= MimeTypeMap.getSingleton()
        return mimetype.getMimeTypeFromExtension(contentResolver.getType(data!!))
    }
}