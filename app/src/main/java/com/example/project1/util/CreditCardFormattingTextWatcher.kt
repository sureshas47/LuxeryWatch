package com.example.project1.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import com.example.project1.R

class CreditCardFormattingTextWatcher(private val editText: EditText, private val cardImageView: ImageView) : TextWatcher {

    companion object {
        private const val SPACE_CHAR = ' '
        private const val SPACE_INTERVAL = 4
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        // No implementation needed
    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        // No implementation needed
    }

    override fun afterTextChanged(editable: Editable) {
        // Remove previous listeners to avoid an infinite loop
        editText.removeTextChangedListener(this)

        // Get the current input without spaces
        val input = editable.toString().replace(" ", "")

        // Format the credit card number with spaces
        val formattedInput = StringBuilder()
        for (i in input.indices) {
            formattedInput.append(input[i])
            if ((i + 1) % SPACE_INTERVAL == 0 && (i + 1) != input.length) {
                formattedInput.append(SPACE_CHAR)
            }
        }
        updateCardImage(editText.text.toString())

        // Update the EditText with the formatted text
        editText.setText(formattedInput.toString())
        editText.setSelection(formattedInput.length)

        // Restore the TextWatcher
        editText.addTextChangedListener(this)
    }

    private fun updateCardImage(cardNumber: String) {
        // Check the first digit of the card number and set the corresponding image
        if (!cardNumber.isEmpty() && cardNumber.length >= 1) {
            val firstDigit = cardNumber[0]
            val cardImage: Int = when (firstDigit) {
                '4' -> R.drawable.visa
                '5' -> R.drawable.mastercard
                // Add more cases for other card types if needed
                else -> R.drawable.credit_card
            }

            // Set the card image in the ImageView
            cardImageView.setImageResource(cardImage)
        }
    }
}