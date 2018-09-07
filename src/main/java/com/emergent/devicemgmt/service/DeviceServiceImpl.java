package com.emergent.devicemgmt.service;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.repository.DeviceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceJpaRepository deviceJpaRepository;

    @Override
    public List<Device> getAllDevices() {
        return deviceJpaRepository.findAll();
    }

    @Override
    public Optional<Device> getDeviceForUniqueId(String uniqueId) {
        return deviceJpaRepository.findByUniqueId(uniqueId);
    }
}
