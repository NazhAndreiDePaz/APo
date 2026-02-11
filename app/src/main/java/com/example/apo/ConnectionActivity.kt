package com.example.apo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

        val imgLogo = findViewById<ImageView>(R.id.imgLogo)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val cardDeviceList = findViewById<View>(R.id.cardDeviceList)

        // 1. PHASE 1: "Searching..." appears after 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val fadeIn = AlphaAnimation(0.0f, 1.0f)
            fadeIn.duration = 500
            tvStatus.startAnimation(fadeIn)
            tvStatus.visibility = View.VISIBLE
        }, 1500)

        // 2. PHASE 2: Hide Logo, Show List (After 3.5 seconds)
        Handler(Looper.getMainLooper()).postDelayed({
            // Fade OUT Logo and Text
            imgLogo.animate().alpha(0f).setDuration(500).withEndAction {
                imgLogo.visibility = View.GONE
            }
            tvStatus.animate().alpha(0f).setDuration(500).withEndAction {
                tvStatus.visibility = View.GONE
            }

            // Fade IN List
            cardDeviceList.alpha = 0f
            cardDeviceList.visibility = View.VISIBLE
            cardDeviceList.animate().alpha(1f).setDuration(500)
        }, 3500)

        // 3. PHASE 3: Click Logic
        val launchDashboard = View.OnClickListener {
            // PASS THE IP ADDRESS (Simulated)
            val ip = "192.168.1.15" // Replace with your real Pi IP later
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("DEVICE_IP", ip)
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.btnDevice1)?.setOnClickListener(launchDashboard)
        findViewById<View>(R.id.btnDevice2)?.setOnClickListener(launchDashboard)
        findViewById<View>(R.id.btnDevice3)?.setOnClickListener(launchDashboard)
    }
}