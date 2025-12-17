package com.arvind.batteryservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.ServiceManager
import android.util.Log
import com.arvind.battery.IArvindBattery
import com.arvind.hal.health.IArvindHealth

class ArvindBatteryService : Service() {
    // Exact name defined in C++ main()
    private val HAL_NAME = "com.arvind.hal.health.IArvindHealth/default"

    private val binder = object : IArvindBattery.Stub() {
        override fun getLivePercentage(): Int {
            return try {
                // Connect to our Custom HAL
                val binder = ServiceManager.waitForDeclaredService(HAL_NAME)
                if (binder == null) return -404
                
                val hal = IArvindHealth.Stub.asInterface(binder)
                hal.kernelCapacity
            } catch (e: Exception) {
                Log.e("ArvindService", "HAL Error", e)
                -500
            }
        }
    }
    override fun onBind(intent: Intent?): IBinder = binder
}