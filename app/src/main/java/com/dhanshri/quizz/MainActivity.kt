package com.dhanshri.quizz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dhanshri.quizz.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding  = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainBinding.btnStart.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnSignOut.setOnClickListener {
            // if user sign with email and password
            FirebaseAuth.getInstance().signOut()

            // if user sign in with google
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Sign out is Successful", Toast.LENGTH_LONG).show()
                }
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}