package com.cargo.service;

import com.cargo.exception.CargoUserNotFoundException;
import com.cargo.model.user.Role;
import com.cargo.model.user.User;
import com.cargo.repos.UserRepo;
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

//    public User findByName(String name){
//        return userRepo.findByUsername(name);
//    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean addUser(User user){
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if(Objects.nonNull(userFromDb)){
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return true;
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }


//    public void updateProfile(User user, String password, String email) {
//        String userEmail = user.getEmail();
//
//        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
//                (userEmail != null && !userEmail.equals(email));
//
//        if (isEmailChanged) {
//            user.setEmail(email);
//        }
//
////        if (!StringUtils.isEmpty(password)) {
//        if (password != null) {
//            user.setPassword(passwordEncoder.encode(password));
//        }
//
//        userRepo.save(user);
//    }
}
