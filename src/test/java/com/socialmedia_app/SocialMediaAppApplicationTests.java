package com.socialmedia_app;

import com.socialmedia_app.dto.UserDTO;
import com.socialmedia_app.model.User;
import com.socialmedia_app.repository.UserRepository;
import com.socialmedia_app.service.impl.UserServiceImpl;
import com.socialmedia_app.utils.Role;
import com.socialmedia_app.utils.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SocialMediaAppApplication.class)
class SocialMediaAppApplicationTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserDTO userDTO = new UserDTO(
                null,
                "john_doe",
                "password123",
                "john@example.com",
                "1234567890",
                false,
                Theme.LIGHT,
                Role.USER,
                new ArrayList<>()
        );

        User user = new User(
                1L,
                "john_doe",
                "encodedPassword123",
                "john@example.com",
                "1234567890",
                false,
                Theme.LIGHT,
                Role.USER,
                new ArrayList<>()
        );

        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO registeredUser = userService.registerUser(userDTO);

        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getUsername()).isEqualTo("john_doe");
        assertThat(registeredUser.getEmail()).isEqualTo("john@example.com");
    }
}