package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.model.enums.Role;
import softuni.exam.drive.repository.UserRepository;

import java.text.MessageFormat;

/**
 * Service used for user related operations
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String EXCEPTION_MESSAGE_FORMAT = "There is already a user with the given {0} ({1})";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates and saves new user to the database
     * @param registerBindingModel user dto
     * @throws RuntimeException if username name is used
     * @throws RuntimeException if email is used
     * @throws RuntimeException if phone number is used
     * @throws RuntimeException if first name & last name combination is used
     */
    public void createUser(RegisterBindingModel registerBindingModel) {
        final String username = registerBindingModel.getUsername();
        final String firstName = registerBindingModel.getFirstName();
        final String lastName = registerBindingModel.getLastName();
        final String email = registerBindingModel.getEmail();
        final String phoneNumber = registerBindingModel.getPhoneNumber();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException(MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "username", username));
        } else if (userRepository.existsByEmail(email)) {
            throw new RuntimeException(MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "email", email));
        } else if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException(MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "phone number", phoneNumber));
        } else if (userRepository.existsByFirstNameAndLastName(firstName, lastName)) {
            throw new RuntimeException(MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "name", firstName + ' ' + lastName));
        }

        final User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(registerBindingModel.getPassword()));
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
    }
}
