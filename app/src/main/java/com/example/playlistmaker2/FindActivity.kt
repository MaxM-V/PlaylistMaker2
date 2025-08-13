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
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class FindActivity : AppCompatActivity() {

    var searchText: String = ""
    lateinit var input:EditText
    val keyStart = "selectionStart"
    val keyEnd ="selectionEnd"
    val currencyTrackList = arrayListOf<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)
        if (NetworkUtils.isInternetAvailable(this)) {
            Toast.makeText(this, "Internet is available", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
        val clearButton= findViewById<ImageView>(R.id.clearIcon)
        input = findViewById<EditText>(R.id.inputSearch)

        val trackList = arrayListOf(
            Track("Smells Like Teen Spirit",  "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track("Billie Jean","Michael Jackson","4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track("Stayin' Alive","Bee Gees","4:10","https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg" ),
            Track("Sweet Child O'Mine","Guns N' Roses","5:03","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
        )

        currencyTrackList.addAll(trackList)

        val recyclerView = findViewById<RecyclerView>(R.id.findrv)
        val adapter = AdapterTrack( currencyTrackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

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
                currencyTrackList.clear()

                if (searchText.isEmpty()) {
                    currencyTrackList.addAll(trackList)
                } else {
                    currencyTrackList.addAll(trackList.filter {
                        it.trackName.contains(searchText, ignoreCase = true) ||
                                it.artistName.contains(searchText, ignoreCase = true)
                    })
                }
                adapter.notifyDataSetChanged()




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


    class AdapterTrack (
        private val trackL: List<Track>
    ) :RecyclerView.Adapter<AdapterTrack.PersonViewHolder>(){
        class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTrack: TextView = itemView.findViewById(R.id.trackname)
            val nameArtists: TextView = itemView.findViewById(R.id.artistname)
            val timeTrack: TextView = itemView.findViewById(R.id.trackTime)
        val trackImage: ImageView = itemView.findViewById(R.id.icon_group)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_search, parent, false)
            return PersonViewHolder(view)
        }

        override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
            val track = trackL[position]
            holder.nameTrack.text = track.trackName
            holder.nameArtists.text = track.artistName
            holder.timeTrack.text = track.trackTime
            val radiusDp = 2f
            val scale = holder.itemView.context.resources.displayMetrics.density
            val radiusPx = (radiusDp * scale).toInt()


            Glide.with(holder.itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(RoundedCorners(radiusPx))
                .into(holder.trackImage)

        }

        override fun getItemCount(): Int = trackL.size
        
    }


        }

