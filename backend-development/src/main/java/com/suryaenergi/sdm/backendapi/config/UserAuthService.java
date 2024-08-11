package com.suryaenergi.sdm.backendapi.config;

import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserAuthService {
    @Autowired
    private EmployeeRepository userRepository;

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmailAndIsActive(username,true)
                        .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
            }
        };
    }
}
