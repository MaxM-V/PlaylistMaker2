package com.example.playlistmaker2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.net.Uri
import android.widget.Button
import android.widget.LinearLayout

class StActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_st)
        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
        val buttonShare = findViewById<LinearLayout>(R.id.share)
        val buttonSwitch = findViewById<LinearLayout>(R.id.switchS)
        val buttonSupport = findViewById<LinearLayout>(R.id.support)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.user)

        buttonBack.setOnClickListener{val dIntent = Intent(this, MainActivity::class.java)
            startActivity(dIntent)
        }

        buttonShare.setOnClickListener{
shareApp()
        }

        buttonSwitch.setOnClickListener{

        }

        buttonSupport.setOnClickListener{
            val recipient = "@string/MailUser"
            val subject = "@string/TextSendToSupport"
            val body = "@string/ThxtoDev"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
                startActivity(intent)
        }
        buttonUserAgreement.setOnClickListener{
val UtrYP = "https://yandex.ru/legal/practicum_offer/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UtrYP))
            startActivity(intent)
        }

    }

    fun shareApp () {
        val shareText = "@string/RecommendYP"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"  // Указываем, что передаём обычный текст
            putExtra(Intent.EXTRA_TEXT, shareText)  // Передаём сам текст в интент
        }
        val chooser = Intent.createChooser(intent, "Поделиться приложением через:")
        startActivity(chooser)
    }
}
