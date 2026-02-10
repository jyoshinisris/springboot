package org.test.crash_course_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CrashCourseSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrashCourseSpringbootApplication.class, args);
	}
}