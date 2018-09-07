package com.emergent.devicemgmt.service;

import com.emergent.devicemgmt.domain.User;
import com.emergent.devicemgmt.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserJpaRepository userJpaRepository;

    @Autowired
    public void setUserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User findByEmail(String email) {
            return userJpaRepository.findOneByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findByEmail(userName);
        if(user == null)
            throw new UsernameNotFoundException(userName);
        return new UserDetailsImpl(user);
    }
}
