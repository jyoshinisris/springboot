package org.test.crash_course_springboot.repo;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.crash_course_springboot.entities.UserEntity;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUsername(String name);
    @Query("select s.id from UserEntity s where s.username=:username")
    Optional<Long> findIdByUsername(@Param("username") String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
