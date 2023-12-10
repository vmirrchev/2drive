package softuni.exam.drive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import softuni.exam.drive.model.entity.User;
import softuni.exam.drive.repository.UserRepository;

import java.text.MessageFormat;

/**
 * UserDetailsService implementation used for security configuration in order to load a specific user by username
 * @author Vasil Mirchev
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(MessageFormat.format("Could not find user with username ({0})", username));
        }

        return user;
    }
}
