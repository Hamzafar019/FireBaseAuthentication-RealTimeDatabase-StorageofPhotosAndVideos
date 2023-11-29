package com.example.myapplication

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivityImageuploaderBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class imageuploader : AppCompatActivity() {
    private lateinit var binding:ActivityImageuploaderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityImageuploaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadbutton.setOnClickListener{
            Toast.makeText(this@imageuploader, "let's Go", Toast.LENGTH_SHORT).show()
            val intent= Intent()
            intent.action=Intent.ACTION_PICK
            intent.type="image/*"
            imagelauncher.launch(intent)}
    }
    val imagelauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){it->
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
        if(it.resultCode== Activity.RESULT_OK){
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show()
            if(it.data!=null){
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()
                val ref=Firebase.storage.reference.child("Photo/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data!!))
                ref.putFile(it.data!!.data!!).addOnSuccessListener{
                    ref.downloadUrl.addOnSuccessListener{

                        Firebase.database.reference.child("Photo").push().setValue(it.toString())
//                        binding.imageView6.setImageURI(it)
                        Picasso.get().load(it.toString()).into(binding.imageView7);
                        Toast.makeText(this@imageuploader, "success", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else{
                Toast.makeText(this@imageuploader, "null", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this@imageuploader, "not working", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileType(data: Uri): String? {
        val mimetype= MimeTypeMap.getSingleton()
        return mimetype.getMimeTypeFromExtension(contentResolver.getType(data!!))
    }
}
