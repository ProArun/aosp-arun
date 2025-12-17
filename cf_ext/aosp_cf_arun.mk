# 1. Inherit from the standard Cuttlefish x86_64 phone product
$(call inherit-product, device/google/cuttlefish/vsoc_x86_64_only/phone/aosp_cf.mk)

# 2. Override Product Information
PRODUCT_NAME := aosp_cf_arun
PRODUCT_DEVICE := vsoc_x86_64
PRODUCT_BRAND := Arun
PRODUCT_MODEL := Arun Cuttlefish Custom

# 3. Add your custom packages
# "HelloCompose" is the module name we will define in the Android.bp later
PRODUCT_PACKAGES += \
    HelloCompose \
	Hello \
	HelloSource \
	HelloJetpackSource \
	RPiTicTacToe \
	usb-can

PRODUCT_PACKAGES += \
    com.arun.common.wifi \
    ArunWifiService \
    ArunWifiClient


# --- Arun Battery Project Modules ---

# 1. The Shared AIDL Library
PRODUCT_PACKAGES += \
    com.arun.battery.lib

# 2. The Manager SDK (Shared Library)
PRODUCT_PACKAGES += \
    ArunBatteryManager

# 3. The Privileged System Service
PRODUCT_PACKAGES += \
    ArunBatteryService

# 4. The Client App
PRODUCT_PACKAGES += \
    ArunBatteryClient

# 5. Copy the Privileged Permissions Whitelist
# (We need to create this file in the Review section below)
PRODUCT_COPY_FILES += \
    device/arun/cf_ext/permissions/privapp-permissions-battery.xml:system_ext/etc/permissions/privapp-permissions-battery.xml


# ARVIND PROJECT MODULES
PRODUCT_PACKAGES += \
    com.arvind.hal.health \
    android.hardware.health.arvind-service \
    com.arvind.battery.lib \
    ArvindBatteryService \
    ArvindBatteryManager \
    ArvindBatteryClient

# # Register the Arvind HAL Manifest
# DEVICE_MANIFEST_FILE += device/arun/arvind/manifest_arvind.xml

# # Register the Framework Compatibility Matrix for Arvind HAL
# #DEVICE_FRAMEWORK_COMPATIBILITY_MATRIX_FILE += device/arun/arvind/framework_compatibility_matrix.xml
# # CORRECT (Works in Product Makefiles)
# DEVICE_PRODUCT_COMPATIBILITY_MATRIX_FILE += device/arun/arvind/framework_compatibility_matrix.xml

PRODUCT_PACKAGES += \
    manifest_arvind.xml


PRODUCT_PACKAGES += arvind_framework_matrix
