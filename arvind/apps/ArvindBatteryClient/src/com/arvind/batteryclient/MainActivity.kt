package com.arvind.batteryclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.arvind.manager.ArvindBatteryManager
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = ArvindBatteryManager(this)
        
        setContent {
            var level by remember { mutableStateOf("Waiting...") }
            
            // Poll every 1 second
            LaunchedEffect(Unit) {
                while(true) {
                    val result = manager.read()
                    level = if (result < 0) "Error: $result" else "$result%"
                    delay(1000)
                }
            }
            
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Arvind Battery: $level", fontSize = 32.sp)
            }
        }
    }
}