package com.emergent.devicemgmt.service;

import com.emergent.devicemgmt.domain.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    List<Device> getAllDevices();

    Optional<Device> getDeviceForUniqueId(String uniqueId);
}
