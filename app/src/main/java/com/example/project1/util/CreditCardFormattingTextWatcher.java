package com.example.project1.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.project1.R;


public class CreditCardFormattingTextWatcher implements TextWatcher {

    private static final char SPACE_CHAR = ' ';
    private static final int SPACE_INTERVAL = 4;

    private EditText editText;

    ImageView cardImageView;

    public CreditCardFormattingTextWatcher(EditText editText, ImageView cardImageView) {
        this.editText = editText;
        this.cardImageView = cardImageView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Remove previous listeners to avoid infinite loop
        editText.removeTextChangedListener(this);

        // Get the current input without spaces
        String input = editable.toString().replaceAll(" ", "");

        // Format the credit card number with spaces
        StringBuilder formattedInput = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            formattedInput.append(input.charAt(i));
            if ((i + 1) % SPACE_INTERVAL == 0 && (i + 1) != input.length()) {
                formattedInput.append(SPACE_CHAR);
            }
        }
        updateCardImage(editText.getText().toString());
        // Update the EditText with the formatted text
        editText.setText(formattedInput.toString());
        editText.setSelection(formattedInput.length());

        // Restore the TextWatcher
        editText.addTextChangedListener(this);
    }

    private void updateCardImage(String cardNumber) {
        // Check the first digit of the card number and set the corresponding image
        if (!cardNumber.isEmpty() && cardNumber.length() >= 1) {
            char firstDigit = cardNumber.charAt(0);
            int cardImage;

            switch (firstDigit) {
                case '4':
                    cardImage = R.drawable.visa;
                    break;
                case '5':
                    cardImage = R.drawable.mastercard;
                    break;
                // Add more cases for other card types if needed
                default:
                    cardImage = R.drawable.credit_card;
                    break;
            }

            // Set the card image in the ImageView
            cardImageView.setImageResource(cardImage);
        }
    }
}
