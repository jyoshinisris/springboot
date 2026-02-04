package org.test.crash_course_springboot.repo;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.test.crash_course_springboot.entities.UserEntity;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUsername(String name);
}
