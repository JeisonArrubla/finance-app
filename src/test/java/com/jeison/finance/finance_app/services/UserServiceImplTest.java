package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.Role;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.RoleRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    User user = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    void testCreateUserDuplicateUsername() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUserSuccess() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role(2L, "ROLE_USER")));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
