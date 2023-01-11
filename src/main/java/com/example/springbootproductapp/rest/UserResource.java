package com.example.springbootproductapp.rest;

import com.example.springbootproductapp.controller.BadRequestException;
import com.example.springbootproductapp.controller.NotFoundException;
import com.example.springbootproductapp.service.UserDTO;
import com.example.springbootproductapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/all", produces = "application/json") //produces-"application/json" это по дефолту
    public List<UserDTO> findAll() {
        return userService.findAll().stream()
                .peek(userDTO -> userDTO.setPassword(null))
                .collect(Collectors.toList());
        //здесь использовать peek и поставить пароль в null
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") Integer id) {
        UserDTO userDTO = userService.findById(id).orElseThrow(NotFoundException::new);
        userDTO.setPassword(null);

        return userDTO;
    }

    @PostMapping(produces = "application/json")
    public UserDTO create(@RequestBody UserDTO userDTO) {
        //нужно чтоб в запросе id не был указан
        if(userDTO.getId() != null) {
            throw new BadRequestException();
        }
        userService.save(userDTO);
        return userDTO;
    }

    @PutMapping(produces = "application/json")
    public void update(@RequestBody UserDTO userDTO) {
        if(userDTO.getId() == null) {
            throw new BadRequestException();
        }
        userService.save(userDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        userService.delete(id);
    }

    @ExceptionHandler
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException ex) {
        return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> badRequestExceptionHandler(BadRequestException ex) {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }
}

//этот класс будет контроллером, но контроллеры для REST принято называть ресорсами
//TODO если из json надо исключить некоторое поле из сущности то использовать @JsonIgnore
