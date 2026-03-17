package com.example.techlounge_checkin_app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var studentCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvCount = findViewById<TextView>(R.id.tvCount)
        val btnCheckIn = findViewById<Button>(R.id.btnCheckIn)
        val btnCheckOut = findViewById<Button>(R.id.btnCheckOut)

        btnCheckIn.setOnClickListener {
            studentCount++
            tvCount.text = studentCount.toString()
        }

        btnCheckOut.setOnClickListener {
            if (studentCount > 0) {
                studentCount--
                tvCount.text = studentCount.toString()
            } else {
                Toast.makeText(this, "No students inside", Toast.LENGTH_SHORT).show()
            }
        }
    }
}