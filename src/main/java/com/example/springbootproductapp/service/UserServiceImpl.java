package com.example.springbootproductapp.service;

import com.example.springbootproductapp.persist.User;
import com.example.springbootproductapp.persist.UserRepository;
import com.example.springbootproductapp.persist.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    //чтобы конструктор внедрялся
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge, Integer page, Integer size) {
        Specification<User> spec = Specification.where(null);

        if(usernameFilter != null && !usernameFilter.isBlank()) {
            spec = spec.and(UserSpecification.usernameLike(usernameFilter));
        }
        if(minAge != null) {
            spec = spec.and(UserSpecification.minAge(minAge));
        }
        if(maxAge != null) {
            spec = spec.and(UserSpecification.maxAge(maxAge));
        }

        return userRepository.findAll(spec, PageRequest.of(page, size))
                .map(UserDTO::new);
    }

    //способ#3
//    @Override
//    public List<UserDTO> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge) {
//        Specification<User> spec = Specification.where(null);
//
//        if(usernameFilter != null && !usernameFilter.isBlank()) {
//            spec = spec.and(UserSpecification.usernameLike(usernameFilter));
//        }
//        if(minAge != null) {
//            spec = spec.and(UserSpecification.minAge(minAge));
//        }
//        if(maxAge != null) {
//            spec = spec.and(UserSpecification.maxAge(maxAge));
//        }
//
//        return userRepository.findAll(spec).stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
//    }

    //это для способ#2
//    @Override
//    public List<UserDTO> findWithFilter(String usernameFilter) {
//        return userRepository.findUserByUsernameLike(usernameFilter).stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<UserDTO> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge) {
//        return userRepository.findWithFilter(usernameFilter, minAge, maxAge).stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
//    }

    //транзакционность надо выносить на сервисный уровень
    @Transactional
    @Override
    public Optional<UserDTO> findById(int id) {
        return userRepository.findById(id)
                .map(UserDTO::new);
//        //это для старого варианта
//        User user = userRepository.findById(id);
//        if(user != null) {
//            return new UserDTO(user);
//        }
//        return null;
    }

    @Transactional
    @Override
    public void save(UserDTO userDTO) {
        User userToSave = new User(userDTO);
        userToSave.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(userToSave);
        if(userDTO.getId() == null) {
            userDTO.setId(userToSave.getId());
        }
//        //старый вариант
//        userRepository.insert(new User(userDTO));
    }


    @Transactional
    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
//        //старый вариант
//        userRepository.delete(id);
    }
}
