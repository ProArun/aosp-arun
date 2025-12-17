#include <android/binder_manager.h>
#include <android/binder_process.h>
#include <android-base/logging.h>

// FIX 1: NDK headers always start with 'aidl/'
#include <aidl/com/arvind/hal/health/BnArvindHealth.h>

#include <fstream>
#include <string>

// FIX 2: NDK namespaces always start with 'aidl::'
using namespace aidl::com::arvind::hal::health;

class ArvindHealthImpl : public BnArvindHealth {
    ::ndk::ScopedAStatus getKernelCapacity(int32_t* _aidl_return) override {
        // Reads from Cuttlefish virtual battery path
        std::ifstream file("/sys/class/power_supply/ubattery/capacity");
        if (file.is_open()) {
            std::string line;
            if (std::getline(file, line)) {
                *_aidl_return = std::stoi(line);
                return ::ndk::ScopedAStatus::ok();
            }
        }
        // Fallback for physical devices or error state
        *_aidl_return = -999; 
        return ::ndk::ScopedAStatus::ok();
    }
};

int main() {
    ABinderProcess_setThreadPoolMaxThreadCount(0);
    std::shared_ptr<ArvindHealthImpl> service = ndk::SharedRefBase::make<ArvindHealthImpl>();
    
    // Register service
    const std::string name = std::string(IArvindHealth::descriptor) + "/default";
    binder_status_t status = AServiceManager_addService(service->asBinder().get(), name.c_str());
    
    if (status != STATUS_OK) {
        LOG(ERROR) << "Failed to register Arvind HAL";
        return -1;
    }

    LOG(INFO) << "Arvind HAL Started: " << name;
    ABinderProcess_joinThreadPool();
    return 0;
}