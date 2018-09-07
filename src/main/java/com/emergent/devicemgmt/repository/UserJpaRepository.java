package com.emergent.devicemgmt.repository;

import com.emergent.devicemgmt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    User findOneByEmail(String email);
}
