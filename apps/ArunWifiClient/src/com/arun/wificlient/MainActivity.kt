package com.arun.wificlient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arun.common.IWifiControl // Import generated AIDL

class MainActivity : ComponentActivity() {

    // The Interface to talk to the service
    private var wifiService: IWifiControl? = null
    private var isBound = mutableStateOf(false)

    // Connection Callback
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            wifiService = IWifiControl.Stub.asInterface(service)
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            wifiService = null
            isBound.value = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind to the Service App automatically on launch
        val intent = Intent("com.arun.wifi.CONTROL_SERVICE")
        intent.setPackage("com.arun.wifiservice")
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        setContent {
            WifiControlScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    @Composable
    fun WifiControlScreen() {
        var statusText by remember { mutableStateOf("Checking...") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Service Bound: ${isBound.value}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                // Call the remote service
                try {
                    val enabled = wifiService?.isWifiEnabled() ?: false
                    statusText = if (enabled) "Wi-Fi is ON" else "Wi-Fi is OFF"
                } catch (e: Exception) {
                    statusText = "Error: ${e.message}"
                }
            }, enabled = isBound.value) {
                Text("Get Status")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = statusText)

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(onClick = { wifiService?.setWifiEnabled(true) }, enabled = isBound.value) {
                    Text("Turn ON")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = { wifiService?.setWifiEnabled(false) }, enabled = isBound.value) {
                    Text("Turn OFF")
                }
            }
        }
    }
}