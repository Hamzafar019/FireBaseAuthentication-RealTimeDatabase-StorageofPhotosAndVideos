package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class signup : AppCompatActivity() {
    private val binding:ActivitySignupBinding by lazy{
        ActivitySignupBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        binding.login.setOnClickListener(){

            startActivity(Intent(this,login::class.java))
            finish()
        }

        binding.register.setOnClickListener(){
            val fullname=binding.fullname.text.toString()
            val username=binding.username.text.toString()
            val password=binding.password.text.toString()
            val confirmpassword=binding.confirmpassword.text.toString()

            if(fullname.isEmpty()||username.isEmpty()||password.isEmpty()||confirmpassword.isEmpty()){
                Toast.makeText(this, "Please fill the empty fields", Toast.LENGTH_SHORT).show()
            }
            else{
                if(password!=confirmpassword){
                    Toast.makeText(this, "Passwords are not same", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(username,password)
                        .addOnCompleteListener(this){task->
                            if(task.isSuccessful){
                                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,login::class.java))
                                finish()
                            }
                            else{
                                Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }

                        }

                }
            }


        }
    }
}