package com.example.project1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.dataClasses.Product
import com.example.project1.rvAdapters.ProductAdapter
import com.example.project1.util.CartManager
import com.example.project1.util.WrapContentGridLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ProductActivity : AppCompatActivity() {
    private var adapter: ProductAdapter? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)




        val cartButton: ImageView = findViewById(R.id.addToCart)
        cartButton.setOnClickListener {
            CartManager.getInstance().navigateToCart(this)
        }

        val searchView: SearchView = findViewById(R.id.searchView);

        val query = FirebaseDatabase.getInstance().reference.child("productList/rolex")
        val options = FirebaseRecyclerOptions.Builder<Product>().setQuery(query, Product::class.java).build()

        // Pass reference to ProductAdapter
        adapter = ProductAdapter(options, this)


        val rView: RecyclerView = findViewById(R.id.recyclerView)
        rView.layoutManager = WrapContentGridLayoutManager(this, 2)
        rView.adapter = adapter


        val rolexReference = FirebaseDatabase.getInstance().reference.child("productList/rolex")



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = rolexReference.orderByChild("name").startAt(newText.orEmpty()).endAt(newText + "\uf8ff")

                // Create new options with the updated query
                val newOptions = FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(query, Product::class.java)
                    .setLifecycleOwner(this@ProductActivity)
                    .build()


                println(newOptions)
                if (newOptions != null) {
                    adapter?.updateOptions(newOptions)
                }
                return true
            }
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