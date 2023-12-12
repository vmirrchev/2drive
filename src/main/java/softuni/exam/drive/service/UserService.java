package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.dto.RegisterBindingModel;
import softuni.exam.drive.model.dto.RoleBindingModel;
import softuni.exam.drive.model.dto.UserBindingModel;
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
    private static final String USERNAME_TAKEN_EXCEPTION_MESSAGE_FORMAT = MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "username", "{0}");
    private static final String EMAIL_TAKEN_EXCEPTION_MESSAGE_FORMAT = MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "email", "{0}");
    private static final String PHONE_NUMBER_TAKEN_EXCEPTION_MESSAGE_FORMAT = MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "phone number", "{0}");
    private static final String NAME_TAKEN_EXCEPTION_MESSAGE_FORMAT = MessageFormat.format(EXCEPTION_MESSAGE_FORMAT, "name", "{0}");
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
            throw new RuntimeException(MessageFormat.format(USERNAME_TAKEN_EXCEPTION_MESSAGE_FORMAT, username));
        } else if (userRepository.existsByEmail(email)) {
            throw new RuntimeException(MessageFormat.format(EMAIL_TAKEN_EXCEPTION_MESSAGE_FORMAT, email));
        } else if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException(MessageFormat.format(PHONE_NUMBER_TAKEN_EXCEPTION_MESSAGE_FORMAT, phoneNumber));
        } else if (userRepository.existsByFirstNameAndLastName(firstName, lastName)) {
            throw new RuntimeException(MessageFormat.format(NAME_TAKEN_EXCEPTION_MESSAGE_FORMAT, firstName + ' ' + lastName));
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

    /**
     * Update the provided user object and save to the database
     * @param user user object to update
     * @param userBindingModel user binding model with update data
     */
    public void updateUser(User user, UserBindingModel userBindingModel) {
        final String username = userBindingModel.getUsername();
        final String firstName = userBindingModel.getFirstName();
        final String lastName = userBindingModel.getLastName();
        final String email = userBindingModel.getEmail();
        final String phoneNumber = userBindingModel.getPhoneNumber();

        if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new RuntimeException(MessageFormat.format(USERNAME_TAKEN_EXCEPTION_MESSAGE_FORMAT, username));
        } else if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new RuntimeException(MessageFormat.format(EMAIL_TAKEN_EXCEPTION_MESSAGE_FORMAT, email));
        } else if (!user.getPhoneNumber().equals(phoneNumber) && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException(MessageFormat.format(PHONE_NUMBER_TAKEN_EXCEPTION_MESSAGE_FORMAT, phoneNumber));
        } else if (!user.getFirstName().equals(firstName) && !user.getLastName().equals(lastName) && userRepository.existsByFirstNameAndLastName(firstName, lastName)) {
            throw new RuntimeException(MessageFormat.format(NAME_TAKEN_EXCEPTION_MESSAGE_FORMAT, firstName + ' ' + lastName));
        }

        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);
    }

    /**
     * Update the provided user object role and save to the database
     * @param user user object to update
     * @param roleBindingModel user role binding model with update data
     */
    public void updateUser(User user, RoleBindingModel roleBindingModel) {
        user.setRole(roleBindingModel.getRole());
        userRepository.save(user);
    }

    /**
     * Get the user object for the given id
     * @param userId user identifier
     * @return User with given id
     * @throws RuntimeException if userId is invalid
     */
    public User getUserById(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User id cannot be null");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format("There is no user for the given id ({0})", userId)));
    }

    /**
     * Get a list of users
     * @return List with all users
     */
    public Object getAllUsers() {
        return userRepository.findAll();
    }
}
