package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.dataClasses.Cart
import com.example.project1.rvAdapters.CartAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CartActivity : AppCompatActivity() {
    private var adapter: CartAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val query = FirebaseDatabase.getInstance().reference.child("productList").child("rolex")

        val options = FirebaseRecyclerOptions.Builder<Cart>().setQuery(query, Cart::class.java).build()
        System.out.println(query)
        System.out.println("options")

        val btnCheckOut : Button = findViewById(R.id.btnCheckOut)

        btnCheckOut.setOnClickListener(View.OnClickListener {
            intent = Intent(applicationContext, CheckOutActivity::class.java)
            startActivity(intent)
        } )

        adapter = CartAdapter(options)

        val rView: RecyclerView = findViewById(R.id.rView)
        rView?.layoutManager = LinearLayoutManager(this )
        rView?.adapter = adapter

    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }
}