package com.dhanshri.quizz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dhanshri.quizz.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.identity.SignInPassword
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpBinding : ActivitySignUpBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        signUpBinding.btnSignUp.setOnClickListener {
            val email = signUpBinding.editTextSignUpEmail.text.toString()
            val password = signUpBinding.editTextSignUpPass.text.toString()

            signupWithFirebase(email, password)
        }
    }

    fun signupWithFirebase(email : String, password: String){
        signUpBinding.progressBarSignUp.visibility = View.VISIBLE
        signUpBinding.btnSignUp.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
                finish()
                signUpBinding.progressBarSignUp.visibility = View.INVISIBLE
                signUpBinding.btnSignUp.isClickable = true
            } else {
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}