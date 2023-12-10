package softuni.exam.drive.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.repository.UserRepository;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class UserDetailsServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);
    private final User user = mock(User.class);
    private final String username = "user";

    @Test
    void loadUserByUsernameShouldReturnUserDetails() {
        when(userRepository.findByUsername(username)).thenReturn(user);

        final UserDetails result = userDetailsService.loadUserByUsername(username);

        verify(userRepository, times(1)).findByUsername(username);
        assertEquals(result, user);
    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundException() {
        final String message = MessageFormat.format("Could not find user with username ({0})", username);
        when(userRepository.findByUsername(username)).thenReturn(null);

        final Throwable thrown = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
        assertEquals(message, thrown.getMessage());
    }
}