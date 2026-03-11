package com.example.temperatureconverter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tempInput = findViewById<EditText>(R.id.etTemp)
        val cToFBtn = findViewById<Button>(R.id.btnCtoF)
        val fToCBtn = findViewById<Button>(R.id.btnFtoC)
        val resultText = findViewById<TextView>(R.id.tvResult)

        fun getTemp(): Double? {
            val value = tempInput.text.toString()
            if (value.isEmpty()) {
                resultText.text = "Please enter temperature"
                return null
            }
            return value.toDouble()
        }

        // Celsius → Fahrenheit
        cToFBtn.setOnClickListener {
            getTemp()?.let { celsius ->
                val fahrenheit = (celsius * 9 / 5) + 32
                resultText.text = "Fahrenheit: %.2f".format(fahrenheit)
            }
        }

        // Fahrenheit → Celsius
        fToCBtn.setOnClickListener {
            getTemp()?.let { fahrenheit ->
                val celsius = (fahrenheit - 32) * 5 / 9
                resultText.text = "Celsius: %.2f".format(celsius)
            }
        }
    }
}