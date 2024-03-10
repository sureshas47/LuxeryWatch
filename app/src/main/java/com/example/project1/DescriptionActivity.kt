package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.project1.dataClasses.Cart
import com.example.project1.dataClasses.Product
import com.example.project1.dataClasses.User
import com.example.project1.util.ImageSliderAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class DescriptionActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        auth = FirebaseAuth.getInstance()

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
//        val productImageView : ImageView = findViewById(R.id.productImageView)

        val backBtn : ImageView = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        })



        watchNameTextView.text = product.name
        caseSizeTextView.text = product.caseSize
        powerReserveTextView.text = product.powerReserver
        waterResistanceTextView.text = product.waterResistance
        priceTextView.text = "$" + product.price.toString()
        watchDescriptionTextView.text = product.description

//        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(product.detailImg)
//        Glide.with(this).load(storRef).into(productImageView)

        // Go To Cart Page
        val goToCartIcon: ImageView = findViewById(R.id.addToCart)
        goToCartIcon.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)        }

        //Add Item to Cart
        val addToCartButton: Button = findViewById(R.id.addToCartBtn)
        addToCartButton.setOnClickListener {
            addToCartAndSaveToFirebase(product)
        }
    }

    private fun addToCartAndSaveToFirebase(product: Product) {
        try {
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

            // Get the current user

            val user: FirebaseUser? = auth.currentUser

            val currentUserUid = user?.uid.toString()

            // Check if the user is logged in
            if (currentUserUid != null) {
                // Retrieve user details from database
                val userRef = FirebaseDatabase.getInstance().reference.child("users").child(currentUserUid)
                userRef.get().addOnSuccessListener { dataSnapshot ->
                    val currentUser = dataSnapshot.getValue(User::class.java)

                    // Check if the user object is not null
                    if (currentUser != null) {
                        var isProductFoundInCart=false
                        // Check if product already available in cart
                        for (cartItem in currentUser.cart) {
                            if (cartItem.name == product.name) {
                                // Update the quantity of the prior product added in cart
                                cartItem.quantity += 1
                                isProductFoundInCart = true
                                break
                            }
                        }

                        // If the product is not found, add it to the cart
                        if (!isProductFoundInCart) {
                            // Add the cart item to the user's cart
                            currentUser.cart.add(cartItem)
                        }



                        // Update the user's cart in the database
                        userRef.setValue(currentUser)
                            .addOnSuccessListener {
                                // Redirect to CartActivity
                                val intent = Intent(this, CartActivity::class.java)
                                startActivity(intent)

                                // Show Success Message After Saving Firebase
                                showToast("Item added to cart successfully")
                            }
                            .addOnFailureListener { e ->
                                showToast("Failed to update user's cart. ${e.message}")
                            }
                    } else {
                        showToast("User object is null")
                    }
                }.addOnFailureListener { e ->
                    showToast("Failed to retrieve user details. ${e.message}")
                }
            } else {
                showToast("User not logged in")
            }
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
            e.printStackTrace()
        }
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