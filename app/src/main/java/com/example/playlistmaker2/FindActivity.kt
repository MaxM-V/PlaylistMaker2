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
import android.view.inputmethod.InputMethodManager
import android.content.Context


class FindActivity : AppCompatActivity() {

    var searchText: String = ""
    lateinit var input:EditText
    val keyStart = "selectionStart"
    val keyEnd ="selectionEnd"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_find)
        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)

        val clearButton= findViewById<ImageView>(R.id.clearIcon)
        input = findViewById<EditText>(R.id.inputSearch)

        buttonBack.setOnClickListener{
            finish()
        }

        clearButton.setOnClickListener {
            input.setText("")
            hideKB()
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
        outState.putInt(keyStart,input.selectionStart)
        outState.putInt(keyEnd,input.selectionEnd)

        outState.putString("SEARCH_TEXT", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
val selectionStart = savedInstanceState.getInt(keyStart)
        val selectionEnd = savedInstanceState.getInt(keyEnd)
        input.setSelection(selectionStart,selectionEnd)
        input.postDelayed(
            {input.requestFocus()}, 300)
    }

    fun hideKB() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(input.windowToken, 0)
    }
        }

