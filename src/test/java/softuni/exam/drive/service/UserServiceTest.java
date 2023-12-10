package softuni.exam.drive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;
import softuni.exam.drive.repository.UserRepository;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class UserServiceTest {

    private final RegisterBindingModel registerBindingModel = mock(RegisterBindingModel.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService = new UserService(userRepository, passwordEncoder);
    private final String username = "user";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String email = "john_doe@yahoo.com";
    private final String phoneNumber = "0888504030";
    private final String password = "password";

    @BeforeEach
    public void setUp() {
        when(registerBindingModel.getUsername()).thenReturn(username);
        when(registerBindingModel.getFirstName()).thenReturn(firstName);
        when(registerBindingModel.getLastName()).thenReturn(lastName);
        when(registerBindingModel.getEmail()).thenReturn(email);
        when(registerBindingModel.getPhoneNumber()).thenReturn(phoneNumber);
        when(registerBindingModel.getPassword()).thenReturn(password);
    }

    @AfterEach
    public void clean() {
        reset(userRepository);
    }

    @Test
    void createUserShouldAddNewUser() {
        final ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        userService.createUser(registerBindingModel);

        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).existsByPhoneNumber(phoneNumber);
        verify(userRepository, times(1)).existsByFirstNameAndLastName(firstName, lastName);
        verify(userRepository).save(argumentCaptor.capture());
        final User result = argumentCaptor.getValue();
        assertEquals(username, result.getUsername());
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail());
        assertEquals(phoneNumber, result.getPhoneNumber());
        assertThat(passwordEncoder.matches(password, result.getPassword())).isTrue();
        assertEquals(Role.ROLE_USER, result.getRole());
    }

    @Test
    void createUserShouldThrowWhenUsernameTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given username ({0})", username);
                when(userRepository.existsByUsername(username)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.createUser(registerBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(0)).existsByEmail(any());
        verify(userRepository, times(0)).existsByPhoneNumber(any());
        verify(userRepository, times(0)).existsByFirstNameAndLastName(any(), any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void createUserShouldThrowWhenEmailTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given email ({0})", email);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.createUser(registerBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(0)).existsByPhoneNumber(any());
        verify(userRepository, times(0)).existsByFirstNameAndLastName(any(), any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void createUserShouldThrowWhenPhoneNumberTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given phone number ({0})", phoneNumber);
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.createUser(registerBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, times(1)).existsByPhoneNumber(any());
        verify(userRepository, times(0)).existsByFirstNameAndLastName(any(), any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void createUserShouldThrowWhenFullNameTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given name ({0})", firstName + ' ' + lastName);
        when(userRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.createUser(registerBindingModel));

        assertEquals(exceptionMessage, thrown.getMessage());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).existsByEmail(any());
        verify(userRepository, times(1)).existsByPhoneNumber(any());
        verify(userRepository, times(1)).existsByFirstNameAndLastName(firstName, lastName);
        verify(userRepository, times(0)).save(any());
    }
}