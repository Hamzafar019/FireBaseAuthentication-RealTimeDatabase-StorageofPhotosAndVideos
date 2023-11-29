package com.example.myapplication

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class login : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
//    private lateinit var googleSignInClient:GoogleSignInClient
    override fun onStart() {
        super.onStart()
        val user:FirebaseUser?=auth.currentUser
        if(user!=null){
            startActivity(Intent(this,home::class.java))
            finish()
            Toast.makeText(this, "Goodie", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()


//        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
//        googleSignInClient= GoogleSignIn.getClient(this,gso)

//        binding.google.setOnClickListener(){
//            Toast.makeText(this, "lesse", Toast.LENGTH_SHORT).show()
//            val signinclient=googleSignInClient.signInIntent
//            Toast.makeText(this, "Ajeeb", Toast.LENGTH_SHORT).show()
//            launcher.launch(signinclient)
//        }

        binding.signup.setOnClickListener(){
            startActivity(Intent(this,signup::class.java))
            finish()
        }
        binding.signin.setOnClickListener(){
            val username=binding.username.text.toString()
            val password=binding.password.text.toString()
            if(username.isEmpty()||password.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(username,password)
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Login completed", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,home::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Sign-in Failed", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }
    }
//    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//    {
//        result->
//        if(result.resultCode== Activity.RESULT_OK)
//        {
//            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            if(task.isSuccessful){
//                val account:GoogleSignInAccount?=task.result
//                val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
//                auth.signInWithCredential(credential).addOnCompleteListener{
//                    if(it.isSuccessful){
//                        startActivity(Intent(this,home::class.java))
//                    }else{
//                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
//
//                    }
//                }
//            }
//            else{
//                Toast.makeText(this, "ajeeb", Toast.LENGTH_SHORT).show()
//            }
//        }
//        else{
//            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
//        }
//    }
}