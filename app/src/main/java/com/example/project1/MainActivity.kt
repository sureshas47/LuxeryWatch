package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find button by ID
        val exploreTimeButton: Button = findViewById(R.id.button)
        // Set OnClickListener to button
        exploreTimeButton.setOnClickListener {
            // Go to Login
            val intent = Intent(this, LoginActivity::class.java)
            // Start LoginActivity
            startActivity(intent)
        }
    }
}