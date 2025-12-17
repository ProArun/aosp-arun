package com.arun.batteryservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.ServiceManager
import android.util.Log
import com.arun.battery.IArunBattery

// Import the HAL Interface
import android.hardware.health.IHealth

class ArunBatteryService : Service() {

    private val TAG = "ArunBatteryService"

    // EXACT Name from your ADB output
    private val SERVICE_NAME = "android.hardware.health.IHealth/default"

    private val binder = object : IArunBattery.Stub() {
        override fun getBatteryLevel(): Int {
            Log.d(TAG, "Attempting to connect to HAL: $SERVICE_NAME")
            
            try {
                // 1. Wait for the service. 
                // We use 'waitForDeclaredService' which works for AIDL HALs.
                val binder = ServiceManager.waitForDeclaredService(SERVICE_NAME)
                
                if (binder == null) {
                    Log.e(TAG, "ServiceManager returned NULL! (Likely SELinux Denial)")
                    return -10 
                }

                // 2. Cast and Call
                val healthHal = IHealth.Stub.asInterface(binder)
                val capacity = healthHal.getCapacity()
                
                Log.d(TAG, "Success! HAL Capacity: $capacity")
                return capacity

            } catch (e: Exception) {
                Log.e(TAG, "Exception connecting to HAL", e)
                return -20
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}