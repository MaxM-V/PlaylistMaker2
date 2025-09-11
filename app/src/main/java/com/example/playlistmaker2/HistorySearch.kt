package com.example.playlistmaker2

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistorySearch(context: Context) {

    val pref = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
    val gson = Gson()
    val key ="history"
    val maxSize = 10
    fun getHistory (): List<Track>{
        val json = pref.getString(key,null)?: return emptyList()
        val type = object : TypeToken<List<Track>>(){}.type
        return gson.fromJson(json, type)
    }

    fun clearHistory (){
        pref.edit().remove(key).apply()
    }

    fun saveTrack(track: Track){
        val history = getHistory().toMutableList()
        history.removeAll{it.trackId == track.trackId}
        history.add(0,track)
        if (history.size > maxSize) {
            history.removeAt(history.size-1)
        }
        pref.edit()
            .putString(key,gson.toJson(history))
            .apply()
    }
}
