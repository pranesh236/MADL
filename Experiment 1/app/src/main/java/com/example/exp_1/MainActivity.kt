package com.example.exp_1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var count=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val counterText=findViewById<TextView>(R.id.counterText)
        val checkIn=findViewById<Button>(R.id.checkIn)
        val checkOut=findViewById<Button>(R.id.checkOut)

        checkIn.setOnClickListener {
            count++
            counterText.text=count.toString()
        }
        checkOut.setOnClickListener {
            if(count>0){
                count--
                counterText.text=count.toString()
            }
        }
    }
}