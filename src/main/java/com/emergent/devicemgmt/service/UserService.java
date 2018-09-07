package com.emergent.devicemgmt.service;

import com.emergent.devicemgmt.domain.User;

public interface UserService {
    public User findByEmail(String email);
}
