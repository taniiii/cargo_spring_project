package com.cargo.controller;

import com.cargo.model.user.Role;
import com.cargo.model.user.User;
import com.cargo.service.UserService;
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
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("userList", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public String userEditform(@PathVariable(value = "id") Long id, Model model){

        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
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
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }


    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam("password") @Pattern(regexp = "((?=.*\\d)(?=.*[A-Za-z]).{8,15})") String password,
            @RequestParam("email") @Pattern(regexp = "(\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,6})") String email) {

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userService.saveUser(user);

        return "redirect:/user/profile";
    }


}
