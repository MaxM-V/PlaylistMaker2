package com.example.playlistmaker2
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MediaPlayerActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying= false
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var currentTime: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var previewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        val buttonBack = findViewById<ImageButton>(R.id.arrowBackPlayer)
        val likeButton = findViewById<ImageButton>(R.id.like)
        playButton = findViewById(R.id.playAndPause)
        pauseButton = findViewById(R.id.Pause)
        currentTime = findViewById(R.id.currentTime)
        val imageAlbume = findViewById<ImageView>(R.id.imageAlbume)


        buttonBack.setOnClickListener {
            finish()
        }


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
            intent.getParcelableExtra("TRACK") as? Track
        }


        val trackName = track?.trackName ?: intent.getStringExtra("TRACK_NAME") ?: "Unknown Track"
        val artistName =
            track?.artistName ?: intent.getStringExtra("ARTIST_NAME") ?: "Unknown Artist"
        val duration = track?.trackTime ?: intent.getStringExtra("DURATION") ?: "00:00"
        val albumName =
            track?.collectionName ?: intent.getStringExtra("ALBUM_NAME") ?: "Unknown Album"
        val releaseDate = track?.releaseDate ?: intent.getStringExtra("RELEASE_DATE") ?: "Unknown"
        val genre = track?.primaryGenreName ?: intent.getStringExtra("GENRE") ?: "Unknown Genre"
        val country = track?.country ?: intent.getStringExtra("COUNTRY") ?: "Unknown"
        val artworkUrl = track?.artworkUrl100 ?: intent.getStringExtra("ARTWORK_URL") ?: ""
        previewUrl = track?.previewUrl ?: ""


        val coverUrl = artworkUrl.replaceAfterLast('/', "512x512bb.jpg")


        playButton.setOnClickListener { startPlayback() }
        pauseButton.setOnClickListener { pausePlayback() }


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

        private fun startPlayback() {

            if (previewUrl.isNullOrEmpty()) {
                // Добавь лог или сообщение — нет ссылки на превью
                return
            }
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(previewUrl)
                    prepareAsync()
                    setOnPreparedListener {
                        start()
                        this@MediaPlayerActivity.isPlaying = true
                        playButton.visibility = ImageButton.INVISIBLE
                        pauseButton.visibility = ImageButton.VISIBLE
                        updateTime()
                    }
                    setOnCompletionListener {
                        stopPlayback()
                    }
                }
            } else {
                mediaPlayer?.start()
                isPlaying = true
                playButton.visibility = ImageButton.INVISIBLE
                pauseButton.visibility = ImageButton.VISIBLE
                updateTime()
            }
        }

        private fun pausePlayback() {
            mediaPlayer?.pause()
            isPlaying = false
            playButton.visibility = ImageButton.VISIBLE
            pauseButton.visibility = ImageButton.GONE
            handler.removeCallbacks(updateTimeRunnable)
        }

        private fun stopPlayback() {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.pause()
            isPlaying = false
            currentTime.text = "00:00"
            playButton.visibility = ImageButton.VISIBLE
            pauseButton.visibility = ImageButton.GONE
            handler.removeCallbacks(updateTimeRunnable)
        }

        private val updateTimeRunnable = object : Runnable {
            override fun run() {
                val pos = mediaPlayer?.currentPosition ?: 0
                currentTime.text = formatTime(pos)
                if (isPlaying) handler.postDelayed(this, 500)
            }
        }

        private fun updateTime() {
            handler.post(updateTimeRunnable)
        }

        private fun formatTime(ms: Int): String {
            val totalSec = ms / 1000
            val min = totalSec / 60
            val sec = totalSec % 60
            return String.format("%d:%02d", min, sec)
        }
    override fun onPause() {
        super.onPause()
        if (isPlaying) pausePlayback()
    }
        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer?.release()
            handler.removeCallbacks(updateTimeRunnable)
        }

}