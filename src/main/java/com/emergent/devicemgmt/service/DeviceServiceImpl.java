package com.emergent.devicemgmt.service;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.DeviceUserAssoc;
import com.emergent.devicemgmt.repository.DeviceJpaRepository;
import com.emergent.devicemgmt.repository.DeviceUserAssocJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceJpaRepository deviceJpaRepository;

    @Autowired
    private DeviceUserAssocJpaRepository deviceUserAssocJpaRepository;

    @Override
    public List<Device> getAllDevices() {
        return deviceJpaRepository.findAll();
    }

    @Override
    public Optional<Device> getDeviceForUniqueId(String uniqueId) {
        return deviceJpaRepository.findByUniqueId(uniqueId);
    }

    @Override
    public void save(Device device) {
        deviceJpaRepository.saveAndFlush(device);
    }

    @Override
    public void save(DeviceUserAssoc deviceUserAssoc) {
        deviceUserAssocJpaRepository.save(deviceUserAssoc);
    }
}
