package org.test.crash_course_springboot.model;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String username;
    public Long createdBy;
    public Long modifiedBy;
}
