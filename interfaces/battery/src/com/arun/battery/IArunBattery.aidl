package com.arun.battery;

interface IArunBattery {
    /**
     * Returns the current battery percentage (0-100) directly from the system.
     */
    int getBatteryLevel();
}