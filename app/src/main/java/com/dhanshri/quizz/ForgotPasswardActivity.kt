package com.dhanshri.quizz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dhanshri.quizz.databinding.ActivityForgotPasswardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.oAuthCredential

class ForgotPasswardActivity : AppCompatActivity() {
    private lateinit var forgotBinding : ActivityForgotPasswardBinding
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        forgotBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_passward)

        forgotBinding.btnResend.setOnClickListener {
            val userEmail = forgotBinding.editTextMailForgot.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "We sent a passward reset mail to your email address", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}