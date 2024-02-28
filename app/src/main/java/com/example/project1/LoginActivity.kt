package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

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
