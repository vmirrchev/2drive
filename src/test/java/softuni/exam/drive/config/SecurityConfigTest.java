package softuni.exam.drive.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void securityFilterChain() throws Exception {
        final HttpSecurity http = mock(HttpSecurity.class);
        final DefaultSecurityFilterChain securityFilterChain = mock(DefaultSecurityFilterChain.class);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);
        when(http.exceptionHandling(any())).thenReturn(http);
        when(http.build()).thenReturn(securityFilterChain);

        final SecurityFilterChain result = securityConfig.securityFilterChain(http);
        verify(http, times(1)).authorizeHttpRequests(any());
        verify(http, times(1)).formLogin(any());
        verify(http, times(1)).logout(any());
        verify(http, times(1)).exceptionHandling(any());
        assertEquals(securityFilterChain, result);
    }

    @Test
    void passwordEncoder() {
        final PasswordEncoder result = securityConfig.passwordEncoder();
        assertThat(result).isInstanceOf(BCryptPasswordEncoder.class);
        assertThat(result).isNotNull();
    }
}