package com.emergent.devicemgmt.repository;

import com.emergent.devicemgmt.domain.DeviceUserAssoc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceUserAssocJpaRepository extends JpaRepository<DeviceUserAssoc, Long> {
}
