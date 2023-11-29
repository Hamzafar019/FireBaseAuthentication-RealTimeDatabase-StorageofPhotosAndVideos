package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class home : AppCompatActivity() {
    private val binding:ActivityHomeBinding by lazy{
        ActivityHomeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signout.setOnClickListener(){
            Toast.makeText(this, "ff", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,login::class.java))

        }
            binding.create.setOnClickListener(){
                startActivity(Intent(this,createnotes::class.java))
            }
            binding.open.setOnClickListener(){
                startActivity(Intent(this,allnotes::class.java))
            }
    }
}