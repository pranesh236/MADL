package com.example.temperature_converter

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTemp = findViewById<EditText>(R.id.etTemp)
        val btnCtoF = findViewById<Button>(R.id.btnCtoF)
        val btnFtoC = findViewById<Button>(R.id.btnFtoC)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnCtoF.setOnClickListener {

            val value = etTemp.text.toString()

            if (value.isEmpty()) {
                Toast.makeText(this, "Enter Temperature", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val celsius = value.toDouble()
            val fahrenheit = (celsius * 9/5) + 32

            tvResult.text = "Fahrenheit: %.2f".format(fahrenheit)
        }

        btnFtoC.setOnClickListener {

            val value = etTemp.text.toString()

            if (value.isEmpty()) {
                Toast.makeText(this, "Enter Temperature", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fahrenheit = value.toDouble()
            val celsius = (fahrenheit - 32) * 5/9

            tvResult.text = "Celsius: %.2f".format(celsius)
        }
    }
}