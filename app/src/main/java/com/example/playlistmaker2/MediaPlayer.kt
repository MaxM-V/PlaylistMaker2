package com.example.playlistmaker2

import android.content.Intent
import android.os.Build
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TRACK", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("TRACK") as? Track}


        val trackName = track?.trackName ?: intent.getStringExtra("TRACK_NAME") ?: "Unknown Track"
        val artistName = track?.artistName ?: intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"
        val duration = track?.trackTime ?: intent.getStringExtra("DURATION") ?: "00:00"
        val albumName = track?.collectionName ?: intent.getStringExtra("ALBUM_NAME") ?: "Unknown Album"
        val releaseDate = track?.releaseDate ?: intent.getStringExtra("RELEASE_DATE") ?: "Unknown"
        val genre = track?.primaryGenreName ?: intent.getStringExtra("GENRE") ?: "Unknown Genre"
        val country = track?.country ?: intent.getStringExtra("COUNTRY") ?: "Unknown"
        val artworkUrl = track?.artworkUrl100 ?: intent.getStringExtra("ARTWORK_URL") ?: ""


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

        val radiusDp = 8f
        val scale = resources.displayMetrics.density
        val radiusPx = (radiusDp * scale).toInt()

        Glide.with(this)
            .load(coverUrl)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(radiusPx))
            .into(imageAlbume)

    }
}