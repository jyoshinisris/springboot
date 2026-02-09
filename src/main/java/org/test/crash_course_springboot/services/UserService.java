package org.test.crash_course_springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.crash_course_springboot.entities.UserEntity;
import org.test.crash_course_springboot.exceptions.ResourceNotFoundException;
import org.test.crash_course_springboot.repo.UserRepo;

import java.util.List;



@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // READ ALL
    public List<UserEntity> getUsers() {
        return userRepo.findAll();
    }

    // CREATE
    public UserEntity createUser(UserEntity user) {
        String username = getCurrentUsername();
        user.setCreatedBy(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setModifiedBy(user.getUsername());
        return userRepo.save(user);
    }

    // READ BY ID
    public UserEntity getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Data Not Found at id: " + id));
    }

    // UPDATE
    public UserEntity updateUser(Long id, UserEntity user) {
        UserEntity existing = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        String username = getCurrentUsername();
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        existing.setModifiedBy(username);

        return userRepo.save(existing);
    }

    // DELETE
    public ResponseEntity<?> deleteUser(Long id) {
        UserEntity existing = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Data Not Found at id: " + id));
        userRepo.delete(existing);
        return ResponseEntity.ok().build();
    }
}