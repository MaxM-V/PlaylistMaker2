package com.example.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Locale

class FindActivity : AppCompatActivity() {

    var searchText: String = ""
    lateinit var input: EditText
    val keyStart = "selectionStart"
    val keyEnd = "selectionEnd"
    val currencyTrackList = arrayListOf<Track>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var notSearch: LinearLayout
    private lateinit var errorInternet: LinearLayout
    private lateinit var adapter: AdapterTrack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        if (NetworkUtils.isInternetAvailable(this)) {
            Toast.makeText(this, "Internet is available", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        input = findViewById(R.id.inputSearch)
        recyclerView = findViewById(R.id.findrv)
        notSearch = findViewById(R.id.notSearch)
        errorInternet = findViewById(R.id.errorInternet)
        val retryButton: Button = findViewById(R.id.retryButton)

        adapter = AdapterTrack(currencyTrackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            input.setText("")
            currencyTrackList.clear()
            adapter.notifyDataSetChanged()
            showState(listVisible = false, notFoundVisible = false, errorVisible = false)
            hideKB()
        }

        retryButton.setOnClickListener {
            performSearch(searchText)
        }
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = input.text.toString()
                performSearch(query)
                true
            } else {
                false
            }
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s.toString()
              //  performSearch(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        input.addTextChangedListener(simpleTextWatcher)
    }

    fun performSearch(query: String) {
        currencyTrackList.clear()
        if (query.isEmpty()) {
            adapter.notifyDataSetChanged()
            showState(listVisible = false, notFoundVisible = false, errorVisible = false)
            return
        }
        showState(listVisible = true, notFoundVisible = false, errorVisible = false)

        val call = RetrofitITunes.apiSearch.searchTrack(searchText)
        call.enqueue(object : retrofit2.Callback<SearchResponseMusic> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponseMusic>,
                response: retrofit2.Response<SearchResponseMusic>
            ) {
                if (response.isSuccessful) {
                    val responseJSON = response.body()
                    if (responseJSON != null) {
                        val tracks = responseJSON.results.map { dto ->
                            Track(
                                dto.trackName,
                                dto.artistName,
                                SimpleDateFormat("mm:ss", Locale.getDefault())
                                    .format(dto.trackTimeMillis.toLong()),
                                dto.artworkUrl100 // картинка альбома
                            )
                        }
                        currencyTrackList.addAll(tracks)
                        adapter.notifyDataSetChanged()

                        if (tracks.isEmpty()) {
                            showState(
                                listVisible = false,
                                notFoundVisible = true,
                                errorVisible = false
                            )
                        } else {
                            showState(
                                listVisible = true,
                                notFoundVisible = false,
                                errorVisible = false
                            )
                        }
                    } else {
                        showState(
                            listVisible = false,
                            notFoundVisible = false,
                            errorVisible = true
                        )
                    }
                } else {
                    showState(listVisible = false, notFoundVisible = false, errorVisible = true)
                }
            }

            override fun onFailure(call: Call<SearchResponseMusic>, t: Throwable) {
                showState(listVisible = false, notFoundVisible = false, errorVisible = true)
            }
        })
    }

    private fun showState(listVisible: Boolean, notFoundVisible: Boolean, errorVisible: Boolean) {
        recyclerView.visibility = if (listVisible) View.VISIBLE else View.GONE
        notSearch.visibility = if (notFoundVisible) View.VISIBLE else View.GONE
        errorInternet.visibility = if (errorVisible) View.VISIBLE else View.GONE
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
        outState.putInt(keyStart, input.selectionStart)
        outState.putInt(keyEnd, input.selectionEnd)
        outState.putString("SEARCH_TEXT", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectionStart = savedInstanceState.getInt(keyStart)
        val selectionEnd = savedInstanceState.getInt(keyEnd)
        input.setSelection(selectionStart, selectionEnd)
        input.postDelayed({ input.requestFocus() }, 300)
    }

    fun hideKB() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(input.windowToken, 0)
    }

    class AdapterTrack(
        private val trackL: List<Track>
    ) : RecyclerView.Adapter<AdapterTrack.PersonViewHolder>() {
        class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTrack: TextView = itemView.findViewById(R.id.trackname)
            val nameArtists: TextView = itemView.findViewById(R.id.artistname)
            val timeTrack: TextView = itemView.findViewById(R.id.trackTime)
            val trackImage: ImageView = itemView.findViewById(R.id.icon_group)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_search, parent, false)
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
