package com.example.panvalidation

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var etPan: EditText
    lateinit var etPincode: EditText
    lateinit var btnValidate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPan = findViewById(R.id.etPan)
        etPincode = findViewById(R.id.etPincode)
        btnValidate = findViewById(R.id.btnValidate)

        btnValidate.setOnClickListener {

            val pan = etPan.text.toString().trim()
            val pincode = etPincode.text.toString().trim()

            // Condition i) Both fields should not be empty
            if (pan.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(this, "Fields should not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Condition ii) PAN should be alphanumeric & exactly 10 characters
            val panRegex = Regex("^[A-Z0-9]{10}$")

            if (!panRegex.matches(pan)) {
                etPan.error = "PAN must be 10 alphanumeric characters"
                return@setOnClickListener
            }

            // Condition iii) Pincode should be numeric & exactly 6 digits
            val pinRegex = Regex("^[0-9]{6}$")

            if (!pinRegex.matches(pincode)) {
                etPincode.error = "Pincode must be 6 digits"
                return@setOnClickListener
            }

            Toast.makeText(this, "Validation Successful ✅", Toast.LENGTH_LONG).show()
        }
    }
}