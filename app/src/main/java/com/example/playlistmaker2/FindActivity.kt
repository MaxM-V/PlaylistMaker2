package com.example.playlistmaker2

import AdapterTrack
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
import retrofit2.Callback
import retrofit2.Response
import java.security.KeyStore.TrustedCertificateEntry
import java.text.SimpleDateFormat
import java.util.Locale

class FindActivity : AppCompatActivity() {

    private var searchText: String = ""
    private lateinit var input: EditText
    private val currencyTrackList = arrayListOf<Track>()

    private lateinit var recyclerSearch: RecyclerView
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var notSearch: LinearLayout
    private lateinit var errorInternet: LinearLayout
    private lateinit var historyTitle: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var adapter: AdapterTrack
    private lateinit var historyAdapter: AdapterTrack
    private lateinit var historySearch: HistorySearch

    private val keyStart = "selectionStart"
    private val keyEnd = "selectionEnd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        historySearch = HistorySearch(this)


        if (NetworkUtils.isInternetAvailable(this)) {
            Toast.makeText(this, "Internet is available", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }


        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val retryButton: Button = findViewById(R.id.retryButton)
        input = findViewById(R.id.inputSearch)
        recyclerSearch = findViewById(R.id.recyclerSearch)
        recyclerHistory = findViewById(R.id.recyclerHistory)
        notSearch = findViewById(R.id.notSearch)
        errorInternet = findViewById(R.id.errorInternet)
        historyTitle = findViewById(R.id.historyTitle)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)


        adapter = AdapterTrack(currencyTrackList) { track ->
            historySearch.saveTrack(track)
        }
        recyclerSearch.layoutManager = LinearLayoutManager(this)
        recyclerSearch.adapter = adapter

        historyAdapter = AdapterTrack(mutableListOf()) { track ->
            historySearch.saveTrack(track)

        }
        recyclerHistory.layoutManager = LinearLayoutManager(this)
        recyclerHistory.adapter = historyAdapter
        updateHistory()

        buttonBack.setOnClickListener {
            finish()
        }


        clearHistoryButton.setOnClickListener {
            historySearch.clearHistory()
            updateHistory()
        }


        clearButton.setOnClickListener {
            input.setText("")
            currencyTrackList.clear()
            adapter.notifyDataSetChanged()
            showSearchResults(false)
            hideKB()
            updateHistory()
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
                searchText = s.toString()
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                if (s.isNullOrEmpty() && input.hasFocus()) {
                    updateHistory()
                } else {
                    showHistory(false)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        input.addTextChangedListener(simpleTextWatcher)


        input.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && input.text.isEmpty()) {
                updateHistory()
            } else {
                showHistory(false)
            }
        }
    }

    fun performSearch(query: String) {
        currencyTrackList.clear()
        if (query.isEmpty()) {
            adapter.notifyDataSetChanged()
            showSearchResults(false)
            return
        }

        showSearchResults(true)

        val call = RetrofitITunes.apiSearch.searchTrack(query)
        call.enqueue(object : Callback<SearchResponseMusic> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponseMusic>,
                response: Response<SearchResponseMusic>
            ) {
                if (response.isSuccessful) {
                    val responseJSON = response.body()
                    if (responseJSON != null) {
                        val tracks = responseJSON.results.map { dto ->
                            Track(
                                dto.trackId,
                                dto.trackName,
                                dto.artistName,
                                SimpleDateFormat("mm:ss", Locale.getDefault())
                                    .format(dto.trackTimeMillis.toLong()),
                                dto.artworkUrl100
                            )
                        }
                        currencyTrackList.addAll(tracks)
                        adapter.notifyDataSetChanged()

                        if (tracks.isEmpty()) {
                            recyclerSearch.visibility = View.GONE
                            notSearch.visibility = View.VISIBLE
                            errorInternet.visibility = View.GONE
                        } else {
                            recyclerSearch.visibility = View.VISIBLE
                            notSearch.visibility = View.GONE
                            errorInternet.visibility = View.GONE
                        }
                    } else {
                        recyclerSearch.visibility = View.GONE
                        notSearch.visibility = View.GONE
                        errorInternet.visibility = View.VISIBLE
                    }
                } else {
                    recyclerSearch.visibility = View.GONE
                    notSearch.visibility = View.GONE
                    errorInternet.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<SearchResponseMusic>, t: Throwable) {
                recyclerSearch.visibility = View.GONE
                notSearch.visibility = View.GONE
                errorInternet.visibility = View.VISIBLE
            }
        })
    }

    fun updateHistory() {
        val history = historySearch.getHistory()
        if (history.isNotEmpty()) {
            historyAdapter.updateList(history)
            showHistory(true)
        } else {
            showHistory(false)
        }
    }

    fun showHistory(show: Boolean) {
        historyTitle.visibility = if (show) View.VISIBLE else View.GONE
        clearHistoryButton.visibility = if (show) View.VISIBLE else View.GONE
        recyclerHistory.visibility = if (show) View.VISIBLE else View.GONE
        recyclerSearch.visibility = View.GONE
        notSearch.visibility = View.GONE
        errorInternet.visibility = View.GONE
    }

    fun showSearchResults(show: Boolean) {
        recyclerSearch.visibility = if (show) View.VISIBLE else View.GONE
        recyclerHistory.visibility = View.GONE
        historyTitle.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
//        notSearch.visibility = View.GONE
//        errorInternet.visibility = View.GONE
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

    private fun hideKB() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(input.windowToken, 0)
    }
}