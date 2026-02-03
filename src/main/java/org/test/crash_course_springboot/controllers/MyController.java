package org.test.crash_course_springboot.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.test.crash_course_springboot.entities.UserEntity;
import org.test.crash_course_springboot.exceptions.ResourceNotFoundException;
import org.test.crash_course_springboot.model.User;
import org.test.crash_course_springboot.repo.UserRepo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class MyController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserEntity> getUsers(){
        return userRepo.findAll();
    }

    @PostMapping("/createstudent")
    public UserEntity createUser(@RequestBody UserEntity user){//@RequestBody UserEntity user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id){
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Data Not Found at id:" + id));
}
    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user){
        UserEntity userData = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with this id:"+id));
        userData.setEmail(user.getEmail());
        userData.setName(user.getName());
        return userRepo.save(userData);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteUser(@PathVariable Long id){
        UserEntity userData=userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Data Not Found at id:" + id));
        userRepo.delete(userData);
        return ResponseEntity.ok().build();
    }
}
