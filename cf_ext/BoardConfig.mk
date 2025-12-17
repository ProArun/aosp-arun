# device/arun/cf_ext/BoardConfig.mk
BOARD_KERNEL_CMDLINE += androidboot.selinux=permissive

# Register Arvind HAL Manifest (Vendor Side)
DEVICE_MANIFEST_FILE += device/arun/arvind/manifest_arvind.xml

# Register Arvind Framework Matrix (System Side)
DEVICE_FRAMEWORK_COMPATIBILITY_MATRIX_FILE += device/arun/arvind/framework_compatibility_matrix.xml

BOARD_VENDOR_SEPOLICY_DIRS += device/arun/cf_ext/sepolicy