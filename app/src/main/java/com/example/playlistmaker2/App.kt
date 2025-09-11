package com.example.playlistmaker2

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application(){

    var darkTheme = false
    private val PREFS_NAME = "playlist_maker_prefs"
    private  val KEY_DARK_THEME = "dark_theme_enabled"
    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val darkThemeEnabled = prefs.getBoolean(KEY_DARK_THEME, false)
        setTheme(darkThemeEnabled)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        setTheme(darkThemeEnabled)

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DARK_THEME, darkThemeEnabled).apply()
    }

}
    private fun setTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }



