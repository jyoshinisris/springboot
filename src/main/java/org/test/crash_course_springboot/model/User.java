package org.test.crash_course_springboot.model;

public class User {

    public User(Long id, String name, String email, String password, String role) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String name;
    private String email;
    private String password;
    private String role;
}
