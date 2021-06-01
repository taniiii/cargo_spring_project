package com.cargo.service;

import com.cargo.model.user.Role;
import com.cargo.model.user.User;
import com.cargo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo; //private final UserRepo userRepo;
   // public UserService(UserRepo userRepo){
   // this.userRepo = userRepo;}
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException(" Employee not found for id :: " + id));
    }

    public User findByName(String name){
        return userRepo.findByUsername(name);
    }

    public boolean addUser(User user){
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if(userFromDb != null){
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return true;
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()) //смотрим какие роли есть вообще
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear(); //очищаем все раннее присутствовавшие роли пользователя

        for(String key : form.keySet()){ //проверяем, что форма содержит роли для пользователя
            if(roles.contains(key)){     //кроме ролей в списке есть токены и ИД, кот. не нужны
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);
        }

//        if (!StringUtils.isEmpty(password)) {
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);
    }
}
