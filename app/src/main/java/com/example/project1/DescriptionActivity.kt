package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.FirebaseDatabase

class DescriptionActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        // Retrieve Product object from the intent
        val product = intent.getSerializableExtra("product") as Product

        // For View Pager Slider
        val imageUrls = product.detailImg
        viewPager = findViewById(R.id.viewPager)
        val dotsLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        if (imageUrls.isNotEmpty()) {
            val adapter = ImageSliderAdapter(supportFragmentManager, imageUrls)
            viewPager.adapter = adapter
            addDotsIndicator(dotsLayout, imageUrls.size)
        }

        // Update with retrieved watch details
        val watchNameTextView: TextView = findViewById(R.id.watchName)
        val caseSizeTextView: TextView = findViewById(R.id.caseSizeTv)
        val powerReserveTextView: TextView = findViewById(R.id.powerReserveTv)
        val waterResistanceTextView: TextView = findViewById(R.id.waterResistanceTv)
        val priceTextView: TextView = findViewById(R.id.priceTv)
        val watchDescriptionTextView: TextView = findViewById(R.id.watchDescription)

        watchNameTextView.text = product.name
        caseSizeTextView.text = product.caseSize
        powerReserveTextView.text = product.powerReserver
        waterResistanceTextView.text = product.waterResistance
        priceTextView.text = "$" + product.price.toString()
        watchDescriptionTextView.text = product.description

        val addToCartButton: Button = findViewById(R.id.addToCart)

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

    private fun addDotsIndicator(dotsLayout: LinearLayout, size: Int) {
        val dots = arrayOfNulls<ImageView>(size)

        for (i in 0 until size) {
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot))

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            dotsLayout.addView(dots[i], params)
        }

        dots[0]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot))

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                for (i in dots.indices) {
                    dots[i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            if (i == position) R.drawable.active_dot else R.drawable.non_active_dot
                        )
                    )
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
}
