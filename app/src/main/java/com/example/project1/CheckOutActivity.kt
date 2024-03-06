package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.util.CreditCardFormattingTextWatcher
import java.util.regex.Pattern



class CheckOutActivity : AppCompatActivity() {
    var subtotal: TextView? = null
    var deliveryFee: TextView? = null
    var total: TextView? = null

    var email: EditText? =
        null
    var address: EditText? = null
    var postalCode: EditText? = null
    var deliveryInstructions: EditText? = null

    var cardHolderName: EditText? = null
    var cardNumber: EditText? = null
    var cardExpiry: EditText? = null
    var cardCVV: EditText? = null

    var cardType: ImageView? = null

    var placeOrder: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        subtotal = findViewById(R.id.checkOutSubtotal)
        deliveryFee = findViewById(R.id.checkOutDeliveryFee)
        total = findViewById(R.id.checkOutTotal)

        email = findViewById (R.id.email)
        address = findViewById (R.id.address)
        postalCode = findViewById (R.id.postalCode)
        deliveryInstructions = findViewById (R.id.deliveryInstructions)

        cardHolderName = findViewById (R.id.cardOwner)
        cardNumber = findViewById (R.id.cardNumber)
        cardExpiry = findViewById (R.id.expiryDate)
        cardCVV = findViewById (R.id.cvv)

        cardType = findViewById(R.id.creditCard)

        placeOrder = findViewById(R.id.placeOrder)

        cardNumber?.addTextChangedListener(CreditCardFormattingTextWatcher(cardNumber!!, cardType!!))

        orderSummary()

        val backBtn : ImageView = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        })
    }

    fun orderSummary() {
        val intent = intent
        subtotal?.text = intent.getStringExtra("subTotal")
        deliveryFee?.text = "$10.00"
        val totalCost = (intent.getStringExtra("total"))?.toDouble()?.plus(10)
        total?.text = totalCost.toString()
    }

    private fun validateFields(): Boolean {
        var isValid = true
        isValid = isValid and validateField(
            email!!, "Enter a valid email address", Patterns.EMAIL_ADDRESS.matcher(
                email!!.text.toString().trim { it <= ' ' }).matches()
        )
        isValid = isValid and validateField(
            address!!, "Address cannot be empty", !TextUtils.isEmpty(
                address!!.text.toString().trim { it <= ' ' })
        )
        isValid = isValid and validateField(
            postalCode!!,
            "Enter a valid postal code",
            Pattern.matches(
                "^[A-Za-z][0-9][A-Za-z] [0-9][A-Za-z][0-9]$",
                postalCode!!.text.toString().trim { it <= ' ' })
        )
        isValid = isValid and validateField(
            deliveryInstructions!!, "Delivery instructions cannot be empty", !TextUtils.isEmpty(
                deliveryInstructions!!.text.toString().trim { it <= ' ' })
        )
        isValid = isValid and validateField(
            cardHolderName!!, "Cardholder name cannot be empty", !TextUtils.isEmpty(
                cardHolderName!!.text.toString().trim { it <= ' ' })
        )
        isValid = isValid and validateField(
            cardNumber!!,
            "Enter a valid card number",
            cardNumber!!.text.toString().trim { it <= ' ' }.length >= 16
        )
        isValid = isValid and validateField(
            cardExpiry!!, "Enter a valid expiry date", !TextUtils.isEmpty(
                cardExpiry!!.text.toString().trim { it <= ' ' })
        )
        isValid = isValid and validateField(
            cardCVV!!,
            "Enter a valid CVV",
            cardCVV!!.text.toString().trim { it <= ' ' }.length == 3
        )

        // Add more validation for other fields as needed
        return isValid
    }

    private fun validateField(editText: EditText, errorMessage: String, isValid: Boolean): Boolean {
        if (!isValid) {
            editText.error = errorMessage
        } else {
            editText.error = null
        }
        return isValid
    }
}