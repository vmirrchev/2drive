package softuni.exam.drive.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import softuni.exam.drive.BaseTest;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class UserDetailsServiceImplTest extends BaseTest {

    private final UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

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