package com.example.packetanalyzer

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.packetanalyzer.databinding.ActivityMainBinding
import com.example.packetanalyzer.service.PacketCaptureService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isCapturing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.startButton.setOnClickListener {
            startPacketCapture()
        }

        binding.stopButton.setOnClickListener {
            stopPacketCapture()
        }

        binding.clearButton.setOnClickListener {
            clearPackets()
        }
    }

    private fun startPacketCapture() {
        lifecycleScope.launch {
            val intent = VpnService.prepare(this@MainActivity)
            if (intent != null) {
                startActivityForResult(intent, REQUEST_VPN)
            } else {
                launchPacketCapture()
            }
        }
    }

    private fun launchPacketCapture() {
        val serviceIntent = Intent(this, PacketCaptureService::class.java)
        startService(serviceIntent)
        isCapturing = true
        updateUI()
    }

    private fun stopPacketCapture() {
        val serviceIntent = Intent(this, PacketCaptureService::class.java)
        stopService(serviceIntent)
        isCapturing = false
        updateUI()
    }

    private fun clearPackets() {
        binding.packetList.text = "Packets cleared"
    }

    private fun updateUI() {
        binding.statusText.text = if (isCapturing) "Capturing..." else "Stopped"
        binding.startButton.isEnabled = !isCapturing
        binding.stopButton.isEnabled = isCapturing
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VPN && resultCode == RESULT_OK) {
            launchPacketCapture()
        }
    }

    companion object {
        private const val REQUEST_VPN = 0x0F
    }
}
