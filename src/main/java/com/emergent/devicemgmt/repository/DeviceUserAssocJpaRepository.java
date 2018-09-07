package com.emergent.devicemgmt.repository;

import com.emergent.devicemgmt.domain.Device;
import com.emergent.devicemgmt.domain.DeviceUserAssoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceUserAssocJpaRepository extends JpaRepository<DeviceUserAssoc, Long> {
}
