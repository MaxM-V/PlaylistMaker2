package com.example.playlistmaker2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MediaPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val buttonBack = findViewById<ImageButton>(R.id.arrowBackPlayer)
        buttonBack.setOnClickListener {
            finish()
        }
        val likeButton = findViewById<ImageButton>(R.id.like)

      var isLiked = false
        likeButton.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                likeButton.setImageResource(R.drawable.like)
            } else {
                likeButton.setImageResource(R.drawable.nolike)
            }
        }
        val trackName = intent.getStringExtra("TRACK_NAME") ?: "Unknown Track"
        val artistName = intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"
        val duration = intent.getStringExtra("DURATION") ?: "00:00"
        val albumName = intent.getStringExtra("ALBUM_NAME") ?: "Unknown Album"
        val releaseDate = intent.getStringExtra("RELEASE_DATE") ?: "Unknown"
        val genre = intent.getStringExtra("GENRE") ?: "Unknown Genre"
        val country = intent.getStringExtra("COUNTRY") ?: "Unknown"
        val artworkUrl = intent.getStringExtra("ARTWORK_URL") ?: ""
        val coverUrl = artworkUrl.replaceAfterLast('/', "512x512bb.jpg")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val trackTitle = findViewById<TextView>(R.id.trackTitle)
        val groupName = findViewById<TextView>(R.id.groupName)
        val timeAll = findViewById<TextView>(R.id.timeAll)
        val nameAlbume = findViewById<TextView>(R.id.nameAlbume)
        val yearNumber = findViewById<TextView>(R.id.yearNumber)
        val styleCurrency = findViewById<TextView>(R.id.styleCurrency)
        val countryCurrency = findViewById<TextView>(R.id.countryCurrency)
        val imageAlbume = findViewById<ImageView>(R.id.imageAlbume)

        trackTitle.text = trackName
        groupName.text = artistName
        timeAll.text = duration
        nameAlbume.text = albumName
        yearNumber.text = releaseDate.take(4)
        styleCurrency.text = genre
        countryCurrency.text = country


        Glide.with(this)
            .load(coverUrl)
            .placeholder(R.drawable.placeholder)
            .into(imageAlbume)

    }
}