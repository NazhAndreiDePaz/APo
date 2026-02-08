package com.example.apo

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// import com.example.apo.databinding.ActivityConnectionBinding // <--- REMOVED

class ConnectionActivity : AppCompatActivity() {

    // UI Variables (No Binding)
    private lateinit var statusText: TextView
    private lateinit var deviceListCard: CardView
    private lateinit var rvDevices: RecyclerView

    // Wi-Fi Managers
    private var manager: WifiP2pManager? = null
    private var channel: WifiP2pManager.Channel? = null
    private var receiver: BroadcastReceiver? = null
    private val intentFilter = IntentFilter()

    // Data
    private val peers = mutableListOf<WifiP2pDevice>()
    private val deviceNames = mutableListOf<String>()
    private lateinit var adapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection) // <--- Standard Layout Loading

        // 1. Link UI elements manually
        statusText = findViewById(R.id.statusText)
        deviceListCard = findViewById(R.id.deviceListCard)
        rvDevices = findViewById(R.id.rvDevices)

        // 2. Initialize Wi-Fi P2P
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
        channel = manager?.initialize(this, mainLooper, null)

        // 3. Setup Adapter
        rvDevices.layoutManager = LinearLayoutManager(this)
        adapter = DeviceAdapter(deviceNames) { deviceName ->
            connectToDevice(deviceName)
        }
        rvDevices.adapter = adapter

        // 4. Add Intent Actions
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)

        // 5. Check Permissions
        checkPermissions()
    }

    private fun checkPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.INTERNET
        )
        // Add "Nearby Devices" permission for Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), 101)
        } else {
            startDiscovery()
        }
    }

    private fun startDiscovery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return

        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                statusText.text = "Scanning for Raspberry Pi..."
            }
            override fun onFailure(reason: Int) {
                statusText.text = "Discovery Failed (Error: $reason)"
            }
        })
    }

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList
        peers.clear()
        peers.addAll(refreshedPeers)

        deviceNames.clear()
        for (device in peers) {
            val displayName = if (device.deviceName.isNullOrEmpty()) device.deviceAddress else device.deviceName
            deviceNames.add(displayName)
        }

        adapter.notifyDataSetChanged()

        if (peers.isNotEmpty()) {
            statusText.visibility = View.INVISIBLE
            deviceListCard.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            manager?.requestPeers(channel, peerListListener)
                        }
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(receiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        receiver?.let { unregisterReceiver(it) }
    }

    private fun connectToDevice(deviceName: String) {
        Toast.makeText(this, "Connecting to $deviceName...", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

// --- ADAPTER REMAINS THE SAME ---
class DeviceAdapter(
    private val devices: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val name = devices[position]
        holder.tvName.text = name
        holder.tvName.textAlignment = View.TEXT_ALIGNMENT_CENTER
        holder.tvName.setPadding(0, 30, 0, 30)
        holder.tvName.setBackgroundResource(R.drawable.border_background)
        val params = holder.tvName.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = 16
        holder.tvName.layoutParams = params
        holder.itemView.setOnClickListener { onClick(name) }
    }

    override fun getItemCount() = devices.size
}