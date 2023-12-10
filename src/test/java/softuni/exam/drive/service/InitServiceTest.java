package softuni.exam.drive.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;
import softuni.exam.drive.repository.BrandRepository;
import softuni.exam.drive.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vasil Mirchev
 */
class InitServiceTest {

    private final BrandRepository brandRepository = mock(BrandRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final InitService initService = new InitService(brandRepository, userRepository, passwordEncoder);
    private final String username = "admin";

    @AfterEach
    public void clean() {
        reset(brandRepository);
    }

    @Test
    void runShouldCreateBrands() {
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        when(brandRepository.findAll()).thenReturn(List.of());

        initService.run();

        verify(brandRepository, times(1)).findAll();
        verify(brandRepository).saveAll(argumentCaptor.capture());
        final List<Brand> brands = argumentCaptor.getValue();
        assertEquals("Bmw", brands.get(0).getName());
        assertEquals("Mercedes", brands.get(1).getName());
        assertEquals("Audi", brands.get(2).getName());
    }

    @Test
    void runShouldNotCreateBrands() {
        when(brandRepository.findAll()).thenReturn(List.of(mock(Brand.class)));

        initService.run();

        verify(brandRepository, times(1)).findAll();
        verify(brandRepository, times(0)).saveAll(any());
    }

    @Test
    void runShouldCreateAdmin() {
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.existsByUsername(username)).thenReturn(false);

        initService.run();

        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository).save(argumentCaptor.capture());
        final User user = argumentCaptor.getValue();
        assertEquals("admin", user.getUsername());
        assertEquals("Vasil", user.getFirstName());
        assertEquals("Mirchev", user.getLastName());
        assertEquals("admin@project.com", user.getEmail());
        assertEquals("0888403020", user.getPhoneNumber());
        assertThat(passwordEncoder.matches("admin", user.getPassword())).isTrue();
        assertEquals(Role.ROLE_ADMIN, user.getRole());
    }

    @Test
    void runShouldNotCreateAdmin() {
        when(userRepository.existsByUsername(username)).thenReturn(true);

        initService.run();

        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(0)).saveAll(any());
    }
}