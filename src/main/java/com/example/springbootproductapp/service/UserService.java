package com.example.springbootproductapp.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> findAll();

    //List<UserDTO> findWithFilter(String usernameFilter);

//    List<UserDTO> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge);

    //для pagination
    Page<UserDTO> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge, Integer page, Integer size);

    Optional<UserDTO> findById(int id);

    void save(UserDTO userDTO);

    void delete(int id);
}
