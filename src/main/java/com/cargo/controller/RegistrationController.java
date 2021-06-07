package com.cargo.controller;

import com.cargo.model.user.User;
import com.cargo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.cargo.controller.TranspController.getErrors;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){

        LOGGER.info("registration page visited");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user,
                          BindingResult bindingResult,
                          Model model){
        if(bindingResult.hasErrors()){
            model.mergeAttributes(getErrors(bindingResult));
            return "registration";
        }

        if(!userService.addUser(user)){
            model.addAttribute("usernameError", "User already exists!");
            return "registration";
        }
        LOGGER.warn("new user was registered: " + user.getUsername());
        return "redirect:/login";
    }
}
