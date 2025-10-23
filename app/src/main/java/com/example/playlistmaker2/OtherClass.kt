package com.example.playlistmaker2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

public class NetworkUtils {

    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }
}


class MusicRequest(val text: String)


    data class TrackITunes(
        val trackId: Int,
        val trackName: String,
                       val artistName: String,
                       val trackTimeMillis: Int,
                       val artworkUrl100: String,
        val collectionName: String?,
        val releaseDate: String?,
        val primaryGenreName: String?,
        val country: String?,
        val previewUrl: String?)


data class SearchResponseMusic(
    val resultCount: Int,
    val results: List<TrackITunes>
)

interface MusicApi {
    @GET("/search")
    fun searchTrack (@Query("term") term:String, @Query("entity") entity: String = "song" ): Call<SearchResponseMusic>

}


