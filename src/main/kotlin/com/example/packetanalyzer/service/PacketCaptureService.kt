package com.example.packetanalyzer.service

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.IBinder
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetAddress
import java.nio.ByteBuffer

class PacketCaptureService : VpnService() {

    private var vpnThread: Thread? = null
    private var isRunning = false
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVpn()
        return START_STICKY
    }

    private fun startVpn() {
        if (isRunning) return
        isRunning = true

        vpnThread = Thread {
            try {
                val builder = Builder()
                builder.addAddress("192.168.1.1", 24)
                builder.addRoute("0.0.0.0", 0)
                builder.addDnsServer("8.8.8.8")
                builder.addDnsServer("8.8.4.4")
                builder.setSession("PacketAnalyzer")

                val vpnInterface: ParcelFileDescriptor? = builder.establish()

                if (vpnInterface != null) {
                    handleVpnPackets(vpnInterface)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        vpnThread?.start()
    }

    private fun handleVpnPackets(vpnInterface: ParcelFileDescriptor) {
        val inputStream = FileInputStream(vpnInterface.fileDescriptor)
        val outputStream = FileOutputStream(vpnInterface.fileDescriptor)
        val packetBuffer = ByteArray(32767)

        while (isRunning) {
            try {
                val length = inputStream.read(packetBuffer)
                if (length > 0) {
                    processPacket(packetBuffer, length)
                    outputStream.write(packetBuffer, 0, length)
                }
            } catch (e: Exception) {
                break
            }
        }
    }

    private fun processPacket(packet: ByteArray, length: Int) {
        try {
            val version = packet[0].toInt() shr 4
            if (version == 4) {
                parseIPv4Packet(packet, length)
            } else if (version == 6) {
                parseIPv6Packet(packet, length)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseIPv4Packet(packet: ByteArray, length: Int) {
        if (length < 20) return

        val protocol = packet[9].toInt() and 0xFF
        val srcIp = "${packet[12].toInt() and 0xFF}.${packet[13].toInt() and 0xFF}.${packet[14].toInt() and 0xFF}.${packet[15].toInt() and 0xFF}"
        val dstIp = "${packet[16].toInt() and 0xFF}.${packet[17].toInt() and 0xFF}.${packet[18].toInt() and 0xFF}.${packet[19].toInt() and 0xFF}"

        val protocolName = when (protocol) {
            6 -> "TCP"
            17 -> "UDP"
            1 -> "ICMP"
            else -> "OTHER"
        }

        val packetInfo = "Protocol: $protocolName | Src: $srcIp | Dst: $dstIp | Size: $length bytes"
        logPacket(packetInfo)
    }

    private fun parseIPv6Packet(packet: ByteArray, length: Int) {
        if (length < 40) return
        val protocolName = "IPv6"
        val packetInfo = "Protocol: $protocolName | Size: $length bytes"
        logPacket(packetInfo)
    }

    private fun logPacket(info: String) {
        scope.launch {
            println("PACKET: $info")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        vpnThread?.join(1000)
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
