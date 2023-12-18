package softuni.exam.drive.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import softuni.exam.drive.BaseTest;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class UserServiceTest extends BaseTest {

    private final UserService userService = new UserService(userRepository, passwordEncoder);

    private final String updateUsername = "update";
    private final String updateFirstName = "first";
    private final String updateLastName = "last";
    private final String updateEmail = "update@mail.com";
    private final String updatePhoneNumber = "0888999999";

    @BeforeEach
    public void setUp() {
        when(registerBindingModel.getUsername()).thenReturn(username);
        when(registerBindingModel.getFirstName()).thenReturn(firstName);
        when(registerBindingModel.getLastName()).thenReturn(lastName);
        when(registerBindingModel.getEmail()).thenReturn(email);
        when(registerBindingModel.getPhoneNumber()).thenReturn(phoneNumber);
        when(registerBindingModel.getPassword()).thenReturn(password);
        when(userBindingModel.getUsername()).thenReturn(updateUsername);
        when(userBindingModel.getFirstName()).thenReturn(updateFirstName);
        when(userBindingModel.getLastName()).thenReturn(updateLastName);
        when(userBindingModel.getEmail()).thenReturn(updateEmail);
        when(userBindingModel.getPhoneNumber()).thenReturn(updatePhoneNumber);
        when(user.getUsername()).thenReturn(username);
        when(user.getFirstName()).thenReturn(firstName);
        when(user.getLastName()).thenReturn(lastName);
        when(user.getEmail()).thenReturn(email);
        when(user.getPhoneNumber()).thenReturn(phoneNumber);
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

        verify(user, times(1)).setUsername(updateUsername);
        verify(user, times(1)).setFirstName(updateFirstName);
        verify(user, times(1)).setLastName(updateLastName);
        verify(user, times(1)).setEmail(updateEmail);
        verify(user, times(1)).setPhoneNumber(updatePhoneNumber);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserShouldThrowWhenUsernameTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given username ({0})", updateUsername);
        when(userRepository.existsByUsername(updateUsername)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(updateUsername);
        verify(user, times(0)).setFirstName(updateFirstName);
        verify(user, times(0)).setLastName(updateLastName);
        verify(user, times(0)).setEmail(updateEmail);
        verify(user, times(0)).setPhoneNumber(updatePhoneNumber);
        verify(userRepository, times(0)).save(user);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void updateUserShouldThrowWhenEmailTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given email ({0})", updateEmail);
        when(userRepository.existsByEmail(updateEmail)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(updateUsername);
        verify(user, times(0)).setFirstName(updateFirstName);
        verify(user, times(0)).setLastName(updateLastName);
        verify(user, times(0)).setEmail(updateEmail);
        verify(user, times(0)).setPhoneNumber(updatePhoneNumber);
        verify(userRepository, times(0)).save(user);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void updateUserShouldThrowWhenPhoneNumberTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given phone number ({0})", updatePhoneNumber);
        when(userRepository.existsByPhoneNumber(updatePhoneNumber)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(updateUsername);
        verify(user, times(0)).setFirstName(updateFirstName);
        verify(user, times(0)).setLastName(updateLastName);
        verify(user, times(0)).setEmail(updateEmail);
        verify(user, times(0)).setPhoneNumber(updatePhoneNumber);
        verify(userRepository, times(0)).save(user);
        assertEquals(exceptionMessage, thrown.getMessage());
    }

    @Test
    void updateUserShouldThrowWhenFullNameTaken() {
        final String exceptionMessage = MessageFormat.format("There is already a user with the given name ({0} {1})", updateFirstName, updateLastName);
        when(userRepository.existsByFirstNameAndLastName(updateFirstName, updateLastName)).thenReturn(true);

        final Throwable thrown = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userBindingModel));

        verify(user, times(0)).setUsername(updateUsername);
        verify(user, times(0)).setFirstName(updateFirstName);
        verify(user, times(0)).setLastName(updateLastName);
        verify(user, times(0)).setEmail(updateEmail);
        verify(user, times(0)).setPhoneNumber(updatePhoneNumber);
        verify(userRepository, times(0)).save(user);
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

    @Test
    void updateUser() {
        userService.updateUser(user, roleBindingModel);

        verify(user).setRole(roleBindingModel.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void getAllUsers() {
        final List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        assertEquals(users, userService.getAllUsers());
    }
}