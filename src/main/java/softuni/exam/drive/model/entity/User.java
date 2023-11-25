package softuni.exam.drive.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import softuni.exam.drive.model.enums.Role;

/**
 * Entity class representing the user database model
 * @author Vasil Mirchev
 */
@Data
@Entity
@Table(name = "users", uniqueConstraints =
        @UniqueConstraint(columnNames = {"firstName", "lastName"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 10)
    @Column(unique = true, nullable = false)
    private String username;

    @Length(min = 3, max = 10)
    @NotNull
    private String firstName;

    @Length(min = 3, max = 10)
    @NotNull
    private String lastName;

    @NotNull
    private String password;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Length(min = 10, max = 10)
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Role role;

}
