package com.example.randomnumbergenerator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val minEdit = findViewById<EditText>(R.id.etMin)
        val maxEdit = findViewById<EditText>(R.id.etMax)
        val generateBtn = findViewById<Button>(R.id.btnGenerate)
        val resultText = findViewById<TextView>(R.id.tvResult)

        generateBtn.setOnClickListener {

            val minStr = minEdit.text.toString()
            val maxStr = maxEdit.text.toString()

            if (minStr.isEmpty() || maxStr.isEmpty()) {
                resultText.text = "Please enter both values"
                return@setOnClickListener
            }

            val min = minStr.toInt()
            val max = maxStr.toInt()

            if (min > max) {
                resultText.text = "Min should be less than Max"
                return@setOnClickListener
            }

            val randomNumber = Random.nextInt(min, max + 1)

            resultText.text = "Random Number: $randomNumber"
        }
    }
}