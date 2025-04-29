package com.example.playlistmaker2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonF = findViewById<Button>(R.id.find)
        val buttonM = findViewById<Button>(R.id.media)
        val buttonS = findViewById<Button>(R.id.sett)


        buttonM.setOnClickListener{ val dIntent = Intent(this, MediaActivity::class.java)
            startActivity(dIntent) }
        buttonF.setOnClickListener{ val dIntent = Intent(this, FindActivity::class.java)
            startActivity(dIntent) }
        buttonS.setOnClickListener{ val dIntent = Intent(this, SettingsActivity::class.java)
            startActivity(dIntent) }

    }


//        buttonS.setOnClickListener{
//            val displayIntent = Intent(this, SettingsActivity::class.java)
//            startActivity(displayIntent)
//        }
//        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) { Toast.makeText(this@MainActivity, "Нажали на кнопку ничего не происходит тыкайте дальше", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        buttonF.setOnClickListener(buttonClickListener)
//
//       buttonM.setOnClickListener {
//           (Toast.makeText(
//               this@MainActivity,
//               "Нажали на кнопку Лямбда",
//               Toast.LENGTH_SHORT
//           ).show())
//       }
//
//        buttonS.setOnClickListener(buttonClickListener)

    }
