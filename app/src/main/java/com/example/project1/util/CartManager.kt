package com.example.project1.util

import android.content.Context
import android.content.Intent
import com.example.project1.CartActivity

class CartManager {

    companion object {
        private val instance = CartManager()

        fun getInstance(): CartManager {
            return instance
        }
    }

    // Add any other cart-related methods and properties here

    fun navigateToCart(context: Context) {
        val intent = Intent(context, CartActivity::class.java)
        context.startActivity(intent)
    }
}