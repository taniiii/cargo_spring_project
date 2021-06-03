package com.cargo.controller;

import com.cargo.model.user.User;
import com.cargo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static com.cargo.controller.TranspController.getErrors;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user,
                          BindingResult bindingResult,
                          Model model){ //Map<String, Object> model)
        if(bindingResult.hasErrors()){
            model.mergeAttributes(getErrors(bindingResult));
            return "registration";
        }

        if(!userService.addUser(user)){              //TODO
            model.addAttribute("usernameError", "User already exists!");   //model.put("message", "User already exists!");
            return "registration";
        }
        return "redirect:/login";
    }
}
