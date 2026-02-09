package com.example.apo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedRecordingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_recordings)

        // 1. Back Button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 2. Setup List RecyclerView
        val rv = findViewById<RecyclerView>(R.id.rvRecordings)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = RecordingAdapter(generateDummyData())
    }

    // Fake Data Generator
    private fun generateDummyData(): List<RecordingModel> {
        return listOf(
            RecordingModel("Kitchen_Motion_01.mp4", "Feb 8, 2026", "00:45s"),
            RecordingModel("LivingRoom_Fall_02.mp4", "Feb 7, 2026", "02:15s"),
            RecordingModel("Hallway_Walk_03.mp4", "Feb 7, 2026", "00:30s"),
            RecordingModel("Bedroom_Sleep_04.mp4", "Feb 6, 2026", "10:00m"),
            RecordingModel("Garden_Motion_05.mp4", "Feb 5, 2026", "01:20s")
        )
    }
}

// --- DATA MODEL ---
data class RecordingModel(val title: String, val date: String, val duration: String)

// --- ADAPTER ---
class RecordingAdapter(private val items: List<RecordingModel>) : RecyclerView.Adapter<RecordingAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDuration: TextView = view.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recording, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvDuration.text = "${item.date} • ${item.duration}"
    }

    override fun getItemCount() = items.size
}