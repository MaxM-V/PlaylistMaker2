package com.example.playlistmaker2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Track (
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTime: String,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
):Parcelable
{
    fun getCoverArtwork(): String {
        return artworkUrl100!!.replaceAfterLast('/', "512x512bb.jpg")
    }
}
