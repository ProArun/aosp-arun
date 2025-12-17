package com.arun.batteryclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arun.manager.ArunBatteryManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Use our custom Manager
    private lateinit var manager: ArunBatteryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        manager = ArunBatteryManager(this)

        setContent {
            BatteryScreen(manager)
        }
    }
}

@Composable
fun BatteryScreen(manager: ArunBatteryManager) {
    var batteryLevel by remember { mutableStateOf("Unknown") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "HAL Battery Status", fontSize = 24.sp)
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(text = "$batteryLevel %", fontSize = 48.sp, style = MaterialTheme.typography.displayMedium)
        
        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            scope.launch {
                val level = manager.getBatteryLevel()
                batteryLevel = level.toString()
            }
        }) {
            Text("Refresh from HAL")
        }
    }
}