package com.emergent.devicemgmt.service;

import com.emergent.devicemgmt.domain.Device;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DeviceService {
    List<Device> getAllDevices();

    Device getDeviceForId(Long deviceId);

    Device getDeviceForUniqueId(String uniqueId);
}
