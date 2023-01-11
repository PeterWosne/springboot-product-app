package com.example.springbootproductapp.controller;

import com.example.springbootproductapp.persist.Role;
import com.example.springbootproductapp.persist.RoleRepository;
import com.example.springbootproductapp.service.UserDTO;
import com.example.springbootproductapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class LoginController {

    private UserService userService;

    private RoleRepository roleRepository;

    @Autowired
    public LoginController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login_form";
    }


    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user_registration_form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, Model model) {

        Role role = roleRepository.findById(3).orElseThrow(NotFoundException::new);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        if(result.hasErrors()) {
            return "user_registration_form";
        }

        if(!user.getPassword().equals(user.getMatchingPassword())) {
            result.rejectValue("password", "", "Password not matching");
            return "user_registration_form";
        }

        try {
            userService.save(user);
        } catch (DataIntegrityViolationException exception) {
            ObjectError error = new ObjectError("globalError", "User with this username or email already exists");
            result.addError(error);
            return "user_registration_form";
        }

        return "redirect:/login";
    }
}
