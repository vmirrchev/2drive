package softuni.exam.drive.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import softuni.exam.drive.model.enums.Role;

import java.util.Collection;
import java.util.List;

/**
 * Entity class representing the user database model
 * @author Vasil Mirchev
 */
@Data
@Entity
@Table(name = "users", uniqueConstraints =
        @UniqueConstraint(columnNames = {"firstName", "lastName"}))
public class User implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
