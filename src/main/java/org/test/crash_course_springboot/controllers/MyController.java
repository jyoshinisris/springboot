package org.test.crash_course_springboot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.test.crash_course_springboot.dto.UserDto;
import org.test.crash_course_springboot.dto.UserDto;
import org.test.crash_course_springboot.services.UserService;

import java.util.List;


@RestController
@RequestMapping("/user")

public class MyController {
    //    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @GetMapping()
    public List<UserDto> getUser(){
        return userService.getAllUsers();
        // return  userRepository.findAll();
    }
    @PostMapping("/createstudent")
    public UserDto setUser(@Valid @RequestBody UserDto user){
        return userService.createUser(user);
        //return userRepository.save(user);

    }
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        //return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with this id:"+id));
        return userService.getUserById(id);
    }
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id ,@RequestBody UserDto user){
//        UserEntity userData=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with this id:"+id));
//        userData.setUserName(user.getUserName());
//        userData.setEmail(user.getEmail());
//        userData.setRole(user.getRole());
//        userData.setPassword(user.getPassword());
        //return userRepository.save(userData);
        return userService.updateUser(id,user);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
//        UserEntity userData=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with this id:"+id));
//        userRepository.delete(userData);
        //return ResponseEntity.ok().build();
        return userService.deleteUser(id);
    }
}