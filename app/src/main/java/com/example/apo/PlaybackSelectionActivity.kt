package com.example.apo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class PlaybackSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback_selection)

        // 1. Back Button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 2. Snapshots Button
        findViewById<CardView>(R.id.cardSnapshots).setOnClickListener {
            Toast.makeText(this, "Opening Snapshots...", Toast.LENGTH_SHORT).show()
            // Future: startActivity(Intent(this, SnapshotGalleryActivity::class.java))
        }

        // 3. Recordings Button
        findViewById<CardView>(R.id.cardRecordings).setOnClickListener {
            Toast.makeText(this, "Opening Recordings...", Toast.LENGTH_SHORT).show()
            // Future: startActivity(Intent(this, VideoGalleryActivity::class.java))
        }
    }
}