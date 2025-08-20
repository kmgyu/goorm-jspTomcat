package io.goorm.backend;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private Timestamp regDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }

    public User() {
    }

    public User(int id, String username, String password, String name, String email, Timestamp regDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.regDate = regDate;
    }
// 생성자, getter, setter 메서드들
    // (기본 생성자, 모든 필드 생성자, getter/setter)
}