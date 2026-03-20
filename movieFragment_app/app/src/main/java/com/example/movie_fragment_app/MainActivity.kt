package com.example.movie_fragment_app

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBasic = findViewById<Button>(R.id.btnBasic)
        val btnAdditional = findViewById<Button>(R.id.btnAdditional)

        // Load Basic Details by default on startup
        if (savedInstanceState == null) {
            loadFragment(BasicFragment())
        }

        btnBasic.setOnClickListener {
            loadFragment(BasicFragment())
        }

        btnAdditional.setOnClickListener {
            loadFragment(AdditionalFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}