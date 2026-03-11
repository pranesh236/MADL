package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var idField: EditText
    private lateinit var validateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.etUsername)
        idField = findViewById(R.id.etId)
        validateBtn = findViewById(R.id.btnValidate)

        validateBtn.setOnClickListener {
            validateInputs()
        }
    }

    private fun validateInputs() {
        val name = username.text.toString().trim()
        val id = idField.text.toString().trim()

        // i) Both fields should not be empty
        if (name.isEmpty() || id.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // ii) Name should contain only alphabets
        if (!name.matches(Regex("^[a-zA-Z]+$"))) {
            username.error = "Name should contain only alphabets"
            return
        }

        // iii) ID should be exactly 4 digits numeric
        if (!id.matches(Regex("^\\d{4}$"))) {
            idField.error = "ID must be exactly 4 digits"
            return
        }

        Toast.makeText(this, "Validation Successful", Toast.LENGTH_SHORT).show()
    }
}