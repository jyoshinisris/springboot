package org.test.crash_course_springboot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.crash_course_springboot.dto.UserDto;
import org.test.crash_course_springboot.entities.UserEntity;
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
        user.setPassword(dto.getPassword());
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
        dto.setCreatedAt(user.getCreatedAt());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setModifiedBy(user.getModifiedBy());
        dto.setModifiedAt(user.getModifiedAt());
        dto.setPassword(user.getPassword());

        return dto;
    }

    public UserDto createUser(UserDto dto) {

        UserEntity user = dtoToEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());
        UserEntity saved = userRepo.save(user);
        saved.setCreatedBy(saved.getId());
        saved.setModifiedBy(saved.getId());
        saved = userRepo.save(saved);


        return entityToDto(saved);
    }

public UserDto updateUser(Long id, UserDto dto) {
    UserEntity update = userRepo.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not Found with this id " + id));

    update.setUsername(dto.getUsername());
    update.setEmail(dto.getEmail());

    if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
        update.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    update.setRole(dto.getRole().toUpperCase());
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tokenUsername = auth.getName();
        log.debug("Logged in username: {}",tokenUsername);

        Long loggedInUserId = userRepo.findIdByUsername(tokenUsername).orElseThrow(null);

        if(loggedInUserId!=null){
            update.setModifiedBy(loggedInUserId);
            if(!loggedInUserId.equals(update.getId())){
                log.warn("Unauthorized update attempt by user ID: {}", loggedInUserId);
                try {
                    throw new UnauthorizedActionException(
                            "Only Logged-In User can update their own profile details."
                    );
                } catch (UnauthorizedActionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        log.debug("username: {}, loggedInUserId: {}", tokenUsername, loggedInUserId);
        update.setName(dto.getFirstName()+" "+dto.getLastName());
        update.setEmail(dto.getEmail());

    UserEntity currentUser = userRepo.findByUsername(tokenUsername)
            .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
    update.setModifiedBy(currentUser.getId());

    return entityToDto(userRepo.save(update));
}
    public List<UserDto> getAllUsers() {

        return userRepo.findAll()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {

        return entityToDto(userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found")));
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