package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FindActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    var searchText: String = ""
    lateinit var input:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find)
        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
       val linearLayoutSearch = findViewById<LinearLayout>(R.id.container)
       val clearButton= findViewById<ImageView>(R.id.clearIcon)
      input = findViewById<EditText>(R.id.inputSearch)

        buttonBack.setOnClickListener{val dIntent = Intent(this, MainActivity::class.java)
        startActivity(dIntent)
        }

        clearButton.setOnClickListener {
            input.setText("")
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        input.addTextChangedListener(simpleTextWatcher)
        // логика по работе с введённым значением
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectionStart",input.selectionStart)
        outState.putInt("selectionEnd",input.selectionEnd)

        outState.putString("SEARCH_TEXT", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
val selectionStart = savedInstanceState.getInt("selectionStart")
        val selectionEnd = savedInstanceState.getInt("selectionEnd")
        input.setSelection(selectionStart,selectionEnd)
        input.postDelayed(
            {input.requestFocus()}, 300)
    }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
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