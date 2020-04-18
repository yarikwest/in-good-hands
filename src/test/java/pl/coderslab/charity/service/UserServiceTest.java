package pl.coderslab.charity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.coderslab.charity.exceptions.EmailExistsException;
import pl.coderslab.charity.model.Role;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.repository.RoleRepository;
import pl.coderslab.charity.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldSaveRegisteredUserAndReturn() {
        //given
        Role role = new Role("ROLE_USER");
        User user = new User();
        user.setEmail("email");
        user.setPassword("pass");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(roleRepository.findByName(anyString())).willReturn(Optional.of(role));
        given(bCryptPasswordEncoder.encode(anyString())).willReturn("password");

        //when
        User savedUser = userService.registerNewUser(user);

        //then
        then(userRepository).should().save(any(User.class));
        assertAll(
                () -> assertThat(savedUser.getPassword()).isEqualTo("password"),
                () -> assertThat(savedUser.getActive()).isFalse(),
                () -> assertThat(savedUser.getRoles()).containsExactly(role)
        );
    }

    @Test
    void ifEmailAlreadyExistsForRegisteredUserThenThrowException() {
        //given
        User user = new User();
        user.setEmail("email");
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        //when //then
        assertThrows(EmailExistsException.class, () -> userService.registerNewUser(user));
    }

    @Test
    void shouldActivateUser() {
        //given
        User user = new User();

        //when
        userService.activateUser(user);

        //then
        then(userRepository).should().save(any(User.class));
        assertThat(user.getActive()).isTrue();
    }
}
