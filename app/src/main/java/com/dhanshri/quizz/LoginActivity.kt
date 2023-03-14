package com.dhanshri.quizz

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dhanshri.quizz.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.SignInPassword
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding : ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    lateinit var googleSignInClient : GoogleSignInClient
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        val textOfGoogleButton = loginBinding.btnGoogleSign.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize = 18F

        // register
        registerActivityForGoogleSignIn()

       loginBinding.btnSignIn.setOnClickListener {
           val userEmail = loginBinding.editTextLoginEmail.text.toString()
           val userPassword = loginBinding.editTextLoginPass.text.toString()

           signInUser(userEmail, userPassword)


       }
        loginBinding.btnGoogleSign.setOnClickListener {
            signInGoogle()
        }
        loginBinding.txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }
        loginBinding.txtForgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPasswardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInUser(userEmail : String, userPassword: String){
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Welcome to Quiz Game", Toast.LENGTH_LONG).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser

        if (user != null){
            Toast.makeText(applicationContext, "Welcome to Quiz Game", Toast.LENGTH_LONG).show()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1094755914209-b8t8q7vimqqb7nd603tmb1rlnt5dd8sk.apps.googleusercontent.com")
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        signIn()
    }

    private fun signIn() {
        val signInIntent : Intent = googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun registerActivityForGoogleSignIn() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == RESULT_OK && data != null){
                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }
            })
    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {
        try {
            val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to Quiz Game", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
            
        } catch (ex : ApiException){
            Toast.makeText(applicationContext, ex.localizedMessage, Toast.LENGTH_LONG).show()

        }

    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(authCredential).addOnCompleteListener {task ->
            if (task.isSuccessful){

//                val user = auth.currentUser

            } else {

            }
        }
    }
}