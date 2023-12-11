package softuni.exam.drive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.model.dto.UserBindingModel;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;
import softuni.exam.drive.repository.UserRepository;

import java.text.MessageFormat;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService = new UserService(userRepository, passwordEncoder);
    private final RegisterBindingModel registerBindingModel = mock(RegisterBindingModel.class);
    private final UserBindingModel userBindingModel = mock(UserBindingModel.class);
    private final User user = mock(User.class);
    private final String username = "user";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String email = "john_doe@yahoo.com";
    private final String phoneNumber = "0888504030";
    private final String password = "password";
    private final Long userId = 1L;
    private final String testValue = "test";

    @BeforeEach
    public void setUp() {
        when(registerBindingModel.getUsername()).thenReturn(username);
        when(registerBindingModel.getFirstName()).thenReturn(firstName);
        when(registerBindingModel.getLastName()).thenReturn(lastName);
        when(registerBindingModel.getEmail()).thenReturn(email);
        when(registerBindingModel.getPhoneNumber()).thenReturn(phoneNumber);
        when(registerBindingModel.getPassword()).thenReturn(password);
        when(userBindingModel.getUsername()).thenReturn(username);
        when(userBindingModel.getFirstName()).thenReturn(firstName);
        when(userBindingModel.getLastName()).thenReturn(lastName);
        when(userBindingModel.getEmail()).thenReturn(email);
        when(userBindingModel.getPhoneNumber()).thenReturn(phoneNumber);
        when(user.getUsername()).thenReturn(testValue);
        when(user.getFirstName()).thenReturn(testValue);
        when(user.getLastName()).thenReturn(testValue);
        when(user.getEmail()).thenReturn(testValue);
        when(user.getPhoneNumber()).thenReturn(testValue);
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

    @Test
    void updateUserShouldUpdateUserData() {
        userService.updateUser(user, userBindingModel);

        verify(user, times(1)).setUsername(username);
        verify(user, times(1)).setFirstName(firstName);
        verify(user, times(1)).setLastName(lastName);
        verify(user, times(1)).setEmail(email);
        verify(user, times(1)).setPhoneNumber(phoneNumber);
    }

    @Test
    void updateUserShouldThrowWhenUsernameTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given username ({0})", username);
        when(userRepository.existsByUsername(username)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(username);
        verify(user, times(0)).setFirstName(firstName);
        verify(user, times(0)).setLastName(lastName);
        verify(user, times(0)).setEmail(email);
        verify(user, times(0)).setPhoneNumber(phoneNumber);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void updateUserShouldThrowWhenEmailTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given email ({0})", email);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(username);
        verify(user, times(0)).setFirstName(firstName);
        verify(user, times(0)).setLastName(lastName);
        verify(user, times(0)).setEmail(email);
        verify(user, times(0)).setPhoneNumber(phoneNumber);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void updateUserShouldThrowWhenPhoneNumberTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given phone number ({0})", phoneNumber);
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(username);
        verify(user, times(0)).setFirstName(firstName);
        verify(user, times(0)).setLastName(lastName);
        verify(user, times(0)).setEmail(email);
        verify(user, times(0)).setPhoneNumber(phoneNumber);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void updateUserShouldThrowWhenFullNameTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given name ({0} {1})", firstName, lastName);
        when(userRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(username);
        verify(user, times(0)).setFirstName(firstName);
        verify(user, times(0)).setLastName(lastName);
        verify(user, times(0)).setEmail(email);
        verify(user, times(0)).setPhoneNumber(phoneNumber);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void getUserByIdShouldReturnUser() {
        final User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertEquals(user, userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByIdShouldThrowWhenIdNull() {
        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.getUserById(null));

        assertEquals(thrown.getMessage(), "User id cannot be null");
        verify(userRepository, times(0)).findById(any());
    }

    @Test
    void getUserByIdShouldThrowWhenIdInvalid() {
        final String exceptionMessage = MessageFormat.format("There is no user for the given id ({0})", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));

        assertEquals(thrown.getMessage(), exceptionMessage);
        verify(userRepository, times(1)).findById(userId);
    }
}