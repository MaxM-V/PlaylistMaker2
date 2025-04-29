package com.example.playlistmaker2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
        buttonBack.setOnClickListener{val dIntent = Intent(this, MainActivity::class.java)
            startActivity(dIntent)
        }
        }
    }
