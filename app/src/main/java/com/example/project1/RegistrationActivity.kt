package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        // Skip Registration and Go to Product
        val skipButton: Button = findViewById(R.id.SkipRegistration)
        skipButton.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }

        // Go to Login
        val login: Button = findViewById(R.id.loginPage)
        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Register button click listener
        val registerButton: Button = findViewById(R.id.registerBtn)
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val emailEditText: EditText = findViewById(R.id.emailTv)
        val passwordEditText: EditText = findViewById(R.id.passwordTv)
        val confirmPasswordEditText: EditText = findViewById(R.id.confirmPasswordTv)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please enter email, password, and confirm password")
            return
        }

        if (password.length <= 6){
            showToast("Password must be at least 6 characters long")
            return
        }

        if (password != confirmPassword) {
            showToast("Password and confirm password do not match")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    val user: FirebaseUser? = auth.currentUser
                    showToast("Registration successful! Please login to continue")
                    navigateToLoginActivity()
                } else {
                    // Failed Message if Signup fails
                    showToast("Registration failed. ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
