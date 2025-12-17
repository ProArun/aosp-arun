lunch 
m installclean
source build/envsetup.sh
lunch aosp_cf_arun-trunk_staging-userdebug
m -j$(nproc)

m ArunBatteryManager ArunBatteryService


cat > device/arun/apps/ArunBatteryService/AndroidManifest.xml <<EOF
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
   package="com.arun.batteryservice">
......
.....
......
 </manifest>
EOF

to check file exist or not 
ls -l device/arun/frameworks/ArunBatteryManager/Android.bp
to stop cuttlefish
stop_cvd

You must first gain root access within the ADB shell session using adb root (only available on userdebug/eng builds).
to get SELinux status
 adb shell getenforce
to disable SELinux //Permissive mode
adb shell setenforce 0
to enable SELinux Enforcing mode
adb shell setenforce 1
Viewing SELinux Denials
When troubleshooting or developing policy, you need to see the "denial" messages in the system logs: 
bash
adb shell dmesg | grep denied
or
bash
adb logcat | grep avc

These logs show which actions were blocked (in enforcing mode) or would have been blocked (in permissive mode). The output can be used with tools like audit2allow to help inform policy adjustments.


To check health sevice i think please verify
 adb shell service list | grep health

to become root user
adb root

Remove this line from this from: /home/arun/aosp/device/google/cuttlefish/shared/sepolicy/vendor/file_contexts

Other than everything is in arun 

# Arvind HAL
/vendor/bin/hw/android\.hardware\.health\.arvind-service   u:object_r:hal_health_default_exec:s0
