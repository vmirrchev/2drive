package softuni.exam.drive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.drive.model.entity.User;

/**
 * Repository responsible for user related CRUD operations
 * @author Vasil Mirchev
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
