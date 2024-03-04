package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
    private var subtotal: Double = 0.0
    private var taxes: Double = 0.0
    private var total: Double = 0.0


    var btnCheckOut: Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        val currentUserUid = currentUser?.uid

        btnCheckOut = findViewById(R.id.btnCheckOut);

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



            //  call setOnDataChangedListener function to inflate Calc
            adapter?.setOnDataChangedListener(object : CartAdapter.OnDataChangedListener {
                override fun onDataChanged() {
                    calculateTotals()
                }
            })



        } else {
            Toast.makeText(this, "Please Login to view the cart", Toast.LENGTH_SHORT).show()
            intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



        btnCheckOut?.setOnClickListener(View.OnClickListener {
            intent = Intent(applicationContext, CheckOutActivity::class.java)
            // Pass subtotal as an extra in the intent
            intent.putExtra("total", subtotal)
            startActivity(intent)
        })

    }

    // Cart calculation
    private fun calculateTotals() {
        subtotal = 0.0
        adapter?.snapshots?.forEach { cartItem ->
            subtotal += cartItem.price
        }
        taxes = subtotal * 0.13 // 13% tax
        total = subtotal + taxes

        val textViewSubTotal: TextView = findViewById(R.id.subTotalTv)
        val textViewTaxes: TextView = findViewById(R.id.taxesTv)
        val textViewTotal: TextView = findViewById(R.id.totalTv)

        textViewSubTotal.text = subtotal.toString()
        textViewTaxes.text = taxes.toString()
        textViewTotal.text = total.toString()
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