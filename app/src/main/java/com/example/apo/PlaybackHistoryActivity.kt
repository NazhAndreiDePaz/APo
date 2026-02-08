package com.example.apo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apo.databinding.ActivityPlaybackHistoryBinding

class PlaybackHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaybackHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaybackHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.rvPlayback.layoutManager = LinearLayoutManager(this)

        // MOCK DATA: Simulating recorded videos on the Raspberry Pi
        val videoList = listOf(
            VideoItem("Fall Detected", "Jan 30, 10:23 AM", "00:45"),
            VideoItem("Movement", "Jan 30, 09:15 AM", "02:10"),
            VideoItem("Door Opened", "Jan 29, 08:00 PM", "01:05"),
            VideoItem("Unknown Sound", "Jan 29, 06:30 PM", "00:30")
        )

        binding.rvPlayback.adapter = PlaybackAdapter(videoList) { video ->
            // Action when a video is clicked
            Toast.makeText(this, "Playing: ${video.title}", Toast.LENGTH_SHORT).show()
        }
    }
}

// --- DATA MODEL ---
data class VideoItem(val title: String, val time: String, val duration: String)

// --- ADAPTER ---
class PlaybackAdapter(
    private val videos: List<VideoItem>,
    private val onClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<PlaybackAdapter.PlaybackViewHolder>() {

    class PlaybackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvEventTitle)
        val tvTime: TextView = view.findViewById(R.id.tvEventTime)
        val tvDuration: TextView = view.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaybackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playback, parent, false)
        return PlaybackViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaybackViewHolder, position: Int) {
        val video = videos[position]
        holder.tvTitle.text = video.title
        holder.tvTime.text = video.time
        holder.tvDuration.text = video.duration

        holder.itemView.setOnClickListener { onClick(video) }
    }

    override fun getItemCount() = videos.size
}