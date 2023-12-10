package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.entity.Brand;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;
import softuni.exam.drive.repository.BrandRepository;
import softuni.exam.drive.repository.UserRepository;

import java.util.List;

/**
 * Initialisation service creating initial brand records in the db
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class InitService implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (brandRepository.findAll().isEmpty()) {
            final Brand bmw = new Brand();
            bmw.setName("Bmw");
            final Brand mercedes = new Brand();
            mercedes.setName("Mercedes");
            final Brand audi = new Brand();
            audi.setName("Audi");

            brandRepository.saveAll(List.of(bmw, mercedes, audi));
        }

        if (!userRepository.existsByUsername("admin")) {
            final User user = new User();
            user.setUsername("admin");
            user.setFirstName("Vasil");
            user.setLastName("Mirchev");
            user.setEmail("admin@project.com");
            user.setPhoneNumber("0888403020");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(Role.ROLE_ADMIN);

            userRepository.save(user);
        }
    }
}
