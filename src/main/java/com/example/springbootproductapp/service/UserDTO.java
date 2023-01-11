package com.example.springbootproductapp.service;

import com.example.springbootproductapp.persist.Role;
import com.example.springbootproductapp.persist.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Integer id;

    @NotEmpty
    private String username;

    @Email
    private String email;

    @NotEmpty
    private String password;

    @JsonIgnore
    @NotEmpty
    private String matchingPassword;

    private Integer age;

    private Set<Role> roles;

    public UserDTO() {}

    public UserDTO(String username) {
        this.username = username;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.roles = user.getRoles();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

//удаляем все что связано с Data JPA
