package com.arun.manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.arun.battery.IArunBattery
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ArunBatteryManager(private val context: Context) {

    private var service: IArunBattery? = null
    private val TAG = "ArunBatteryManager"

    // Functional Interface callback for connection status
    var onConnected: (() -> Unit)? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "Connected to ArunBatteryService")
            service = IArunBattery.Stub.asInterface(binder)
            onConnected?.invoke()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "Disconnected from ArunBatteryService")
            service = null
        }
    }

    init {
        // Auto-connect when Manager is initialized
        val intent = Intent()
        // MUST Match the Service Package and Class name exactly
        intent.component = ComponentName(
            "com.arun.batteryservice", 
            "com.arun.batteryservice.ArunBatteryService"
        )
        // BIND_AUTO_CREATE ensures the service starts if it's not running
        val result = context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        Log.d(TAG, "Binding to service result: $result")
    }

    fun getBatteryLevel(): Int {
        if (service == null) {
            Log.e(TAG, "Service is not bound yet!")
            return -100 
        }
        return service?.batteryLevel ?: -1
    }
}