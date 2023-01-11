package com.example.springbootproductapp.controller;

import com.example.springbootproductapp.persist.RoleRepository;
import com.example.springbootproductapp.service.UserDTO;
import com.example.springbootproductapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@EnableGlobalMethodSecurity(securedEnabled = true) //защищаем методы типа апдейты чтоб не отправили запросы
public class UserController {
    //TODO добавить логгер
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {

        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listPage(Model model, @RequestParam("usernameFilter") Optional<String> usernameFilter,
                           @RequestParam("minAgeFilter") Optional<Integer> minAgeFilter,
                           @RequestParam("maxAgeFilter") Optional<Integer> maxAgeFilter,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        logger.info("List page requested");


        Page<UserDTO> users = userService.findWithFilter(
                usernameFilter.filter(s -> !s.isBlank()).orElse(null),
                minAgeFilter.orElse(null),
                maxAgeFilter.orElse(null),
                page.orElse(1) - 1,
                size.orElse(3)
        );
        model.addAttribute("users", users);
        return "user";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") Integer id, Model model) {
        logger.info("Edit page requested");

        model.addAttribute("user", userService.findById(id).orElseThrow(NotFoundException::new));
        model.addAttribute("roles", roleRepository.findAll()); // также добавим в апдейт и нью чтоб не было бага
        return "user_form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, Model model) { //аннотация @ModelAttribute передает в представление имя атрибута
        logger.info("Update endpoint requested");

        model.addAttribute("roles", roleRepository.findAll());

        if(result.hasErrors()) {
            return "user_form";
        }

        if(!user.getPassword().equals(user.getMatchingPassword())) {
            result.rejectValue("password", "", "Password not matching");
            return "user_form";
        }

        logger.info("Updating user with id{}", user.getId());


        try {
            userService.save(user);
        } catch (DataIntegrityViolationException exception) {
            ObjectError error = new ObjectError("globalError", "User with this username or email already exists");
            result.addError(error);
            return "user_form";
        }

        return "redirect:/user";
    }

    @GetMapping("/new")
    public String create(Model model) {
        logger.info("Create new user request");

        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleRepository.findAll());
        return "user_form";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        logger.info("User delete request");

        userService.delete(id);
        return "redirect:/user";
    }


    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex) {
        ModelAndView mav = new ModelAndView("not_found");
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }
}


