package com.example.checkbox

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cbPizza = findViewById<CheckBox>(R.id.cbPizza)
        val cbBurger = findViewById<CheckBox>(R.id.cbBurger)
        val cbCoffee = findViewById<CheckBox>(R.id.cbCoffee)
        val btnOrder = findViewById<Button>(R.id.btnOrder)

        btnOrder.setOnClickListener {
            var totalAmount = 0
            val result = StringBuilder()
            result.append("Selected Items:")

            if (cbPizza.isChecked) {
                totalAmount += 150
                result.append("\nPizza: 150")
            }
            if (cbBurger.isChecked) {
                totalAmount += 120
                result.append("\nBurger: 120")
            }
            if (cbCoffee.isChecked) {
                totalAmount += 80
                result.append("\nCoffee: 80")
            }

            result.append("\nTotal: $totalAmount")
            
            Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
