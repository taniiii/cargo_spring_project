package com.cargo.service;

import com.cargo.exception.CargoUserNotFoundException;
import com.cargo.model.user.Role;
import com.cargo.model.user.User;
import com.cargo.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
//import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepo userRepo;
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
                .orElseThrow(() -> new CargoUserNotFoundException(id.toString())); //TODO
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean addUser(User user){
        LOGGER.info("Trying to create new user, username: " + user.getUsername());
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if(Objects.nonNull(userFromDb)){
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        LOGGER.warn("New user, username: " + user.getUsername() + " was created");

        return true;
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }
}
