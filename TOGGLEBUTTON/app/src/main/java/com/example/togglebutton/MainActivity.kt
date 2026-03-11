package com.example.togglebutton

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        val messageTextView = findViewById<TextView>(R.id.messageTextView)

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Toggle ON → Message is displayed
                messageTextView.visibility = View.VISIBLE
            } else {
                // Toggle OFF → Message is hidden
                messageTextView.visibility = View.GONE
            }
        }
    }
}
