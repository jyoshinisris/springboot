//package org.test.crash_course_springboot.services;
//
//@Component
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity user = userRepo.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
//        return new MyUserDetails(user); }// return custom UserDetails with ID
    //import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.test.crash_course_springboot.entities.UserEntity;
//import org.test.crash_course_springboot.repo.UserRepo;
//
//import java.util.Collection;
//import java.util.Collections;
//
//// Custom UserDetails that exposes numeric ID
//class MyUserDetails implements UserDetails {
//    private final Long id;
//    private final String username;
//    private final String password;
//    private final String authorities;
//
//    public MyUserDetails(UserEntity user) {
//        this.id = user.getId();
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//        this.authorities = user.getRole();
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    @Override
//    public String getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() { return true; }
//
//    @Override
//    public boolean isAccountNonLocked() { return true; }
//
//    @Override
//    public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public boolean isEnabled() { return true; }
//}
//
//}
package org.test.crash_course_springboot.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.test.crash_course_springboot.entities.UserEntity;
import org.test.crash_course_springboot.repo.UserRepo;

import java.util.Collections;
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user =userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        // return new User(user.getUserName(),user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("USER_ROLE")));
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }
}