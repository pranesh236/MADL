package com.example.college_login_validation

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnValidate = findViewById<Button>(R.id.btnValidate)

        btnValidate.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Condition 1: Not Empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Condition 2: Proper College Email (example: ends with .edu or college domain)
            val collegeEmailRegex = Regex("^[A-Za-z0-9._%+-]+@.+\\.edu$")
            if (!collegeEmailRegex.matches(email)) {
                etEmail.error = "Enter valid college email ID"
                return@setOnClickListener
            }

            // Condition 3: Password Validation
            val passwordRegex =
                Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#\$%^&+=!]).{12,}$")

            if (!passwordRegex.matches(password)) {
                etPassword.error =
                    "Password must have 12 chars, 1 uppercase, 1 number, 1 special symbol"
                return@setOnClickListener
            }

            Toast.makeText(this, "Validation Successful", Toast.LENGTH_LONG).show()
        }
    }
}