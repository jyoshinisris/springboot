package org.test.crash_course_springboot.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.test.crash_course_springboot.entities.UserEntity;
import org.test.crash_course_springboot.security.JwtUtil;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserEntity user){
        log.info("Login request received for username {}", user.getUsername());
        try{
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtil.generateToken(userDetails);

            log.info("Login successful for username {}", user.getUsername());

            return ResponseEntity.ok(Map.of("{}",token));

        }catch (Exception e){
            log.error("Exception - {}",e.getMessage());
            return new ResponseEntity<>(Map.of("error",e.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }
}
