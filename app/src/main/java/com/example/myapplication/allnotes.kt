package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityAllnotesBinding
import com.example.myapplication.databinding.UpdatenoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class allnotes : AppCompatActivity(), notesadapter.OnItemClickListener{
    private val binding:ActivityAllnotesBinding by lazy{
        ActivityAllnotesBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var dataBaseReference:DatabaseReference
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        recyclerView=binding.notesRecyclerView
        recyclerView.layoutManager=LinearLayoutManager(this)
        dataBaseReference=FirebaseDatabase.getInstance().reference
        auth=FirebaseAuth.getInstance()
        val currentuser=auth.currentUser
        currentuser?.let { user->
            val noteReference=dataBaseReference.child("Users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notelist= mutableListOf<notes>()
                    for (notesnapshot in snapshot.children){
                        val note=notesnapshot.getValue(notes::class.java)
                        note?.let{
                            notelist.add(it)
                        }
                        notelist.reverse()
                        val adapter=notesadapter(notelist,this@allnotes)
                        recyclerView.adapter=adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    override fun onDeleteClick(nodeid: String) {
        val currentuser=auth.currentUser
        currentuser?.let{user->
            val notereference=dataBaseReference.child("Users").child(user.uid).child("notes")
            notereference.child(nodeid).removeValue()
        }
    }

    override fun onUpdateClick(nodeid: String,title: String, description:String) {

        val dialogbinding=UpdatenoteBinding.inflate(LayoutInflater.from(this))
        val dialog=AlertDialog.Builder(this).setView(dialogbinding.root)
            .setTitle("Update Notes")
            .setPositiveButton("Update"){dialog,_->
                val title=dialogbinding.updatetitle.text.toString()
                val description=dialogbinding.updatedescription.text.toString()
                updateNoteDataBase(nodeid,title,description)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .create()

        dialogbinding.updatetitle.setText(title)
        dialogbinding.updatedescription.setText(description)
        dialog.show()


    }

    private fun updateNoteDataBase(nodeid: String, title: String, description: String) {
        val currentuser=auth.currentUser
        currentuser?.let{user->
            val notereference=dataBaseReference.child("Users").child(user.uid).child("notes")
            val updatenote=notes(title,description,nodeid)
            notereference.child(nodeid).setValue(updatenote)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Note Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}