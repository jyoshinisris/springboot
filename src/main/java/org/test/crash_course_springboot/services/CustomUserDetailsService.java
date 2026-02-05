package org.test.crash_course_springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.test.crash_course_springboot.entities.UserEntity;
import org.test.crash_course_springboot.repo.UserRepo;

import java.util.Collections;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        return new User(user.getUsername(),user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("USER_ROLE")));
    }
}
