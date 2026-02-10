package org.test.crash_course_springboot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.test.crash_course_springboot.dto.UserDto;
import org.test.crash_course_springboot.entities.UserEntity;
import org.test.crash_course_springboot.exceptions.ResourceNotFoundException;
import org.test.crash_course_springboot.repo.UserRepo;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserEntity user){
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        user.setRole(user.getRole());
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        user.setCreatedBy(user.getId());
        user.setModifiedBy(user.getId());
        return userRepo.save(user);
    }

    public UserEntity updateUser(Long id,UserEntity user){
        UserEntity update = userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not Found with this id " + id));

        update.setUsername(user.getUsername());
        update.setEmail(user.getEmail());
        update.setPassword(passwordEncoder.encode(user.getPassword()));
        update.setRole(user.getRole());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tokenUsername = auth.getName();

        UserEntity currentUser = userRepo.findByUsername(tokenUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
        update.setModifiedBy(currentUser.getId());

        return userRepo.save(update);


    }
    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ResponseEntity<?> deleteUser(Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<?> deleteAllUsers() {
        userRepo.deleteAll();
        return ResponseEntity.ok().build();
    }


}