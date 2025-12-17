package com.arvind.manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.arvind.battery.IArvindBattery

class ArvindBatteryManager(context: Context) {
    private var service: IArvindBattery? = null

    init {
        val intent = Intent()
        intent.component = ComponentName("com.arvind.batteryservice", "com.arvind.batteryservice.ArvindBatteryService")
        context.bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(n: ComponentName?, b: IBinder?) {
                service = IArvindBattery.Stub.asInterface(b)
            }
            override fun onServiceDisconnected(n: ComponentName?) { service = null }
        }, Context.BIND_AUTO_CREATE)
    }

    fun read(): Int = service?.livePercentage ?: -1
}