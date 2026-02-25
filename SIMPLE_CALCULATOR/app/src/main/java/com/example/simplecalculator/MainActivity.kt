package com.example.simplecalculator

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val num1 = findViewById<EditText>(R.id.etNum1)
        val num2 = findViewById<EditText>(R.id.etNum2)

        val add = findViewById<Button>(R.id.btnAdd)
        val sub = findViewById<Button>(R.id.btnSub)
        val mul = findViewById<Button>(R.id.btnMul)
        val div = findViewById<Button>(R.id.btnDiv)

        val result = findViewById<TextView>(R.id.tvResult)

        fun getNumbers(): Pair<Double, Double>? {
            val n1 = num1.text.toString()
            val n2 = num2.text.toString()

            if (n1.isEmpty() || n2.isEmpty()) {
                result.text = "Enter both numbers"
                return null
            }
            return Pair(n1.toDouble(), n2.toDouble())
        }

        add.setOnClickListener {
            getNumbers()?.let {
                result.text = "Result: ${it.first + it.second}"
            }
        }

        sub.setOnClickListener {
            getNumbers()?.let {
                result.text = "Result: ${it.first - it.second}"
            }
        }

        mul.setOnClickListener {
            getNumbers()?.let {
                result.text = "Result: ${it.first * it.second}"
            }
        }

        div.setOnClickListener {
            getNumbers()?.let {
                if (it.second == 0.0) {
                    result.text = "Cannot divide by zero"
                } else {
                    result.text = "Result: ${it.first / it.second}"
                }
            }
        }
    }
}