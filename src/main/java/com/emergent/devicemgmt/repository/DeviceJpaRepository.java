package com.emergent.devicemgmt.repository;

import com.emergent.devicemgmt.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceJpaRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUniqueId(String uniqueId);
}
