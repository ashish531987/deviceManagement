package com.emergent.devicemgmt.repository;

import com.emergent.devicemgmt.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Role findOneByRole(String role);
}
