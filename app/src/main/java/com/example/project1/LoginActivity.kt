package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.dataClasses.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        ;

        // Check if user already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToProductActivity()
            return
        }

        // Skip Login and Go to Product
        val skipButton: Button = findViewById(R.id.SkipBtn)
        skipButton.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }

        // Go to Signup
        val signUp: Button = findViewById(R.id.signupBtn)
        signUp.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // Login button click listener
        val loginButton: Button = findViewById(R.id.loginBtn)
        loginButton.setOnClickListener {
            loginUser()
        }


//        Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("961480815572-op5knfdu53jh3alaoeardaj2um17cl82.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val googleSignInBtn: MaterialButton = findViewById(R.id.googleSignIn)

        googleSignInBtn.setOnClickListener({
            signInGoogle()
        })

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            val statusCode = e.statusCode
            Log.w("Google sign in", "signInResult:failed code=$statusCode")
            showToast("Sign in failed. Error code: $statusCode")
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Google log in", "Sucess")
                showToast("Login successful!")

                val user: FirebaseUser? = auth.currentUser

                // Create a User object
                val userDetails = User(
                    uid = user?.uid ?: "",
                    email = user?.email.toString(),
                    // Add more fields as needed
                )

                val userRef =
                    FirebaseDatabase.getInstance().reference.child("users").child(user?.uid ?: "")
                userRef.setValue(userDetails)
                    .addOnSuccessListener {
                        showToast("Log in Successful")
                        navigateToProductActivity()
                    }
                    .addOnFailureListener { e ->
                        showToast("Log in failed. ${e.message}")
                    }

            } else {
                Log.i("Google log in", "fail")
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun loginUser() {
        val emailEditText: EditText = findViewById(R.id.emailTv)
        val passwordEditText: EditText = findViewById(R.id.passwordTv)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter email and password")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // SignIn success
                    val user: FirebaseUser? = auth.currentUser
                    showToast("Login successful!")
                    navigateToProductActivity()
                } else {
                    // Displaying Failed Message if SignIn Fails
                    showToast("Login failed. ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToProductActivity() {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    }
}
