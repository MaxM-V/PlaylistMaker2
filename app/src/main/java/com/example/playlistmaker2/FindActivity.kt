package com.example.playlistmaker2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FindActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find)

        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
        buttonBack.setOnClickListener{val dIntent = Intent(this, MainActivity::class.java)
        startActivity(dIntent)
        }


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
        }
    }



//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//
//        val buttonF = findViewById<Button>(R.id.find)
//        val buttonM = findViewById<Button>(R.id.media)
//        val buttonS = findViewById<Button>(R.id.sett)
//
//        buttonS.setOnClickListener{ val dIntent = Intent(this, SettingsActivity::class.java)
//            startActivity(dIntent) }
//        buttonM.setOnClickListener{ val dIntent = Intent(this, MediaActivity::class.java)
//            startActivity(dIntent) }
//        buttonF.setOnClickListener{ val dIntent = Intent(this, FindActivity::class.java)
//            startActivity(dIntent) }
//
//
//    }