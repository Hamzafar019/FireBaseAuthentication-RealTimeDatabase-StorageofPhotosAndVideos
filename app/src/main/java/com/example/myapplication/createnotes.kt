package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityCreatenotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class createnotes : AppCompatActivity() {
    private lateinit var databasereference:DatabaseReference
    private lateinit var auth:FirebaseAuth
    private val binding:ActivityCreatenotesBinding by lazy{
        ActivityCreatenotesBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databasereference=FirebaseDatabase.getInstance().reference
        auth=FirebaseAuth.getInstance()

        binding.addnotes.setOnClickListener(){
            val notes_title=binding.noteTitle.text.toString()
            val notes_description=binding.noteDescription.text.toString()

            if(notes_title.isEmpty()||notes_description.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }

            else{

                var currentUser=auth.currentUser
                currentUser?.let { user->
                    val notekey=databasereference.child("Users").child(user.uid).child("notes").push().key
                    val noteitem=notes(notes_title,notes_description,notekey?:"")
                    if(notekey!=null){
                        databasereference.child("Users").child(user.uid).child("notes").child(notekey).setValue(noteitem)
                            .addOnCompleteListener{task->
                                if(task.isSuccessful){
                                    Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else{
                                    Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
                                }

                            }
                    }
                }
            }
        }
    }
}