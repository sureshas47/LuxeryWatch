package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ConfirmationActivity : AppCompatActivity() {

    var productsBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        productsBtn = findViewById(R.id.backToProducts)

        productsBtn?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        })


    }
}