package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.project1.dataClasses.Cart
import com.example.project1.dataClasses.Product
import com.example.project1.util.ImageSliderAdapter
import com.google.firebase.database.FirebaseDatabase

class DescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        // Retrieve Product object from the intent
        val product = intent.getSerializableExtra("product") as Product

        // For View Pager Slider
        val imageUrls = product.detailImg
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        if (imageUrls.isNotEmpty()) {
            val adapter = ImageSliderAdapter(supportFragmentManager, imageUrls)
            viewPager.adapter = adapter
        }

        // Update with retrieved watch details
        val watchNameTextView: TextView = findViewById(R.id.watchName)
        val caseSizeTextView: TextView = findViewById(R.id.caseSizeTv)
        val powerReserveTextView: TextView = findViewById(R.id.powerReserveTv)
        val waterResistanceTextView: TextView = findViewById(R.id.waterResistanceTv)
        val priceTextView: TextView = findViewById(R.id.priceTv)
        val watchDescriptionTextView: TextView = findViewById(R.id.watchDescription)
//        val productImageView : ImageView = findViewById(R.id.productImageView)

        watchNameTextView.text = product.name
        caseSizeTextView.text = product.caseSize
        powerReserveTextView.text = product.powerReserver
        waterResistanceTextView.text = product.waterResistance
        priceTextView.text = "$" + product.price.toString()
        watchDescriptionTextView.text = product.description

//        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(product.detailImg)
//        Glide.with(this).load(storRef).into(productImageView)

        val addToCartButton: ImageView = findViewById(R.id.addToCart)

        addToCartButton.setOnClickListener {
            // Pass product object to add to cart function
            addToCart(product)
        }
    }

    private fun addToCart(product: Product) {
        val cartItem = Cart(
            name = product.name,
            price = product.price,
            braceletMaterial = product.braceletMaterial,
            cardImg = product.cardImg,
            caseSize = product.caseSize,
            dialColor = product.dialColor,
            powerReserver = product.powerReserver,
            waterResistance = product.waterResistance
        )
        // Save cart data in firebase
        saveToFirebase(cartItem)
        // Show Success Message After Saving in Firebase
        showToast("Item added to cart successfully")
    }

    private fun saveToFirebase(cartItem: Cart) {
        val cartRef = FirebaseDatabase.getInstance().reference.child("userCart").push()
        cartRef.setValue(cartItem)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}