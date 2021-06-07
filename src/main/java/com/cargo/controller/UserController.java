package com.cargo.controller;

import com.cargo.model.user.Role;
import com.cargo.model.user.User;
import com.cargo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Validated   //??
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("userList", userService.findAll());
        LOGGER.info("All users page was visited by administrator");
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public String userEditform(@PathVariable(value = "id") Long id, Model model){

        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        LOGGER.info("User status edit page was visited by administrator");
        return "useredit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam("newRole") String role,
            @RequestParam("userId") User user){

        user.getRoles().clear(); //очищаем все раннее присутствовавшие роли пользователя
        user.getRoles().add(Role.valueOf(role));

        userService.saveUser(user);
        LOGGER.info("User status was changed, user id: " + user.getId());
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        LOGGER.info("User profile page was visited by user " + user.getUsername());
        return "profile";
    }


    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam("password") String password,
            @RequestParam("email") String email) {
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userService.saveUser(user);
        LOGGER.info("User profile was updated, user: " + user.getUsername());
        return "redirect:/user/profile";
    }


}
