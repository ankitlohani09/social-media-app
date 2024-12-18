package com.socialmedia_app.repository;

import com.socialmedia_app.utils.Role;
import com.socialmedia_app.utils.Theme;
import com.socialmedia_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByTheme(Theme theme);
    List<User> findByRole(Role role);
}
