package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.dataClasses.Cart
import com.example.project1.rvAdapters.CartAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class CartActivity : AppCompatActivity() {
    private var adapter: CartAdapter? = null
    private lateinit var auth: FirebaseAuth

    var btnCheckOut: Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        val currentUserUid = currentUser?.uid

        btnCheckOut = findViewById(R.id.btnCheckOut);

        val backBtn : ImageView = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        })

        if (currentUserUid != null) {

            val query =
                FirebaseDatabase.getInstance().reference.child("users").child(currentUserUid)
                    .child("cart")
            val options =
                FirebaseRecyclerOptions.Builder<Cart>().setQuery(query, Cart::class.java).build()

//            System.out.println(query)
//            System.out.println("options")

            val btnCheckOut: Button = findViewById(R.id.btnCheckOut)

            btnCheckOut.setOnClickListener(View.OnClickListener {
                intent = Intent(applicationContext, CheckOutActivity::class.java)
                startActivity(intent)
            })

            adapter = CartAdapter(options)

            val rView: RecyclerView = findViewById(R.id.rView)
            rView?.layoutManager = LinearLayoutManager(this)
            rView?.adapter = adapter

        } else {
            Toast.makeText(this, "Please Login to view the cart", Toast.LENGTH_SHORT).show()
            intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnCheckOut?.setOnClickListener(View.OnClickListener {
            intent = Intent(applicationContext, CheckOutActivity::class.java)
            startActivity(intent)
        })

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