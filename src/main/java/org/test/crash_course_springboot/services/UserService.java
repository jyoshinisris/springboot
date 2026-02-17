package org.test.crash_course_springboot.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.crash_course_springboot.dto.UserDto;
import org.test.crash_course_springboot.entities.UserEntity;
import org.test.crash_course_springboot.exceptions.DuplicateResourceException;
import org.test.crash_course_springboot.exceptions.ResourceNotFoundException;
import org.test.crash_course_springboot.exceptions.UnauthorizedActionException;
import org.test.crash_course_springboot.repo.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity dtoToEntity(UserDto dto) {
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setName(dto.getFirstName() + " " + dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(dto.getPassword()); // encoded later
        return user;
    }


    private UserDto entityToDto(UserEntity user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        if (user.getName() != null && user.getName().contains(" ")) {
            String[] parts = user.getName().split(" ", 2);
            dto.setFirstName(parts[0]);
            dto.setLastName(parts[1]);
        } else {
            dto.setFirstName(user.getName());
            dto.setLastName("");
        }
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPassword(user.getPassword());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setModifiedBy(user.getModifiedBy());
        return dto;
    }


    public UserDto createUser(UserDto dto)throws DuplicateResourceException {

        UserEntity user = dtoToEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        if (userRepo.existsByUsername(user.getUsername())){
            throw new DuplicateResourceException("UserName already exists");
        }
        if (userRepo.existsByEmail(user.getEmail())){
            throw new DuplicateResourceException("Email already exists");
        }
        UserEntity saved = userRepo.save(user);

        saved.setCreatedBy(saved.getId());
        saved.setModifiedBy(saved.getId());

        saved = userRepo.save(saved);

        return entityToDto(saved);
    }


    public UserDto updateUser(Long id, UserDto dto) throws UnauthorizedActionException {

        UserEntity update = userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tokenUsername = auth.getName();

        UserEntity currentUser = userRepo.findByUsername(tokenUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));


        if (!update.getCreatedBy().equals(currentUser.getId())) {
            log.warn("Unauthorized access by user {}", currentUser.getId());
            throw new UnauthorizedActionException("You can update only your own profile");
        }

        update.setUsername(dto.getUsername());
        update.setName(dto.getFirstName()+" "+dto.getLastName());
        update.setEmail(dto.getEmail());
        update.setRole(dto.getRole().toUpperCase());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            update.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        update.setModifiedBy(currentUser.getId());

        return entityToDto(userRepo.save(update));
    }


    public List<UserDto> getAllUsers() throws UnauthorizedActionException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new UnauthorizedActionException("Only admin can see this detail");
        }

        return userRepo.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) throws UnauthorizedActionException {
        UserEntity current = userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        if (!current.getCreatedBy().equals(currentUser.getId()) &&
                !"ADMIN".equalsIgnoreCase(currentUser.getRole())&&
                !"OWNER".equalsIgnoreCase(currentUser.getRole())
        ) {
            throw new UnauthorizedActionException("Only created user can see their profile.");
        }

        return entityToDto(
                userRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("User not found with id " + id))
        );
    }



    public ResponseEntity<?> deleteUser(Long id) {
        UserEntity delete = userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tokenUsername = auth.getName();

        UserEntity currentUser = userRepo.findByUsername(tokenUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));


        if (!delete.getCreatedBy().equals(currentUser.getId())) {
            log.warn("Unauthorized access by user {}", currentUser.getId());
            try {
                throw new UnauthorizedActionException("Only created user can delete their profile");
            } catch (UnauthorizedActionException e) {
                throw new RuntimeException(e);
            }
        }
        userRepo.deleteById(id);
        return ResponseEntity.ok("User deleted");
    }

    public ResponseEntity<?> deleteAllUsers() throws UnauthorizedActionException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        if (!"OWNER".equalsIgnoreCase(currentUser.getRole())) {
            throw new UnauthorizedActionException("Only admin can see this detail");
        }
        userRepo.deleteAll();
        return ResponseEntity.ok("All users deleted");
    }
}