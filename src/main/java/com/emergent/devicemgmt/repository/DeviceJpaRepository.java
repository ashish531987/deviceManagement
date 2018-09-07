package com.emergent.devicemgmt.repository;

import com.emergent.devicemgmt.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceJpaRepository extends JpaRepository<Device, Long> {
    Device findOneByUniqueId(String uniqueId);
}
