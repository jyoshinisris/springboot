package org.test.crash_course_springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.test.crash_course_springboot.entities.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity,Long> {
}
