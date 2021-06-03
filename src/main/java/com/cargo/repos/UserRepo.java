package com.cargo.repos;

import com.cargo.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
