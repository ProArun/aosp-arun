package com.arun.wifiservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import com.arun.common.IWifiControl

class WifiService : Service() {

    private lateinit var wifiManager: WifiManager

    override fun onCreate() {
        super.onCreate()
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    // Implement the AIDL Stub
    private val binder = object : IWifiControl.Stub() {
        override fun isWifiEnabled(): Boolean {
            return wifiManager.isWifiEnabled
        }

        override fun setWifiEnabled(enable: Boolean): Boolean {
            // setWifiEnabled is deprecated for normal apps, but works for System Apps
            return wifiManager.setWifiEnabled(enable)
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}