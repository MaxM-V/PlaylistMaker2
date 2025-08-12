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

        setContentView(R.layout.activity_st)
        val buttonBack = findViewById<ImageButton>(R.id.arrowBack)
        val buttonShare = findViewById<LinearLayout>(R.id.share)
        val buttonSwitch = findViewById<LinearLayout>(R.id.switchS)
        val buttonSupport = findViewById<LinearLayout>(R.id.support)
        val buttonUserAgreement = findViewById<LinearLayout>(R.id.user)


        buttonBack.setOnClickListener{
            finish()
        }

        buttonShare.setOnClickListener{
shareApp()
        }

        buttonSwitch.setOnClickListener{

        }

        buttonSupport.setOnClickListener{
            val recipient = getString(R.string.MailUser) //"@string/MailUser"
            val subject = getString(R.string.TextSendToSupport) //"@string/TextSendToSupport"
            val body = getString(R.string.ThxtoDev)// "@string/ThxtoDev"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.mailTo))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
                startActivity(intent)
        }
        buttonUserAgreement.setOnClickListener{
val UtrYP = getString(R.string.URLYPLegal)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(UtrYP))
            startActivity(intent)
        }

    }

    fun shareApp () {
        val shareText = getString(R.string.RecommendYP) //"@string/RecommendYP"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        val chooser = Intent.createChooser(intent, getString(R.string.ShareTitle))
        startActivity(chooser)
    }
}
