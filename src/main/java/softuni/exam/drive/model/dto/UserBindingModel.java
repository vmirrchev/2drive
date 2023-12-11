package softuni.exam.drive.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Binding model used for data transfer for user data update
 * @author Vasil Mirchev
 */
@Data
public class UserBindingModel {

    @NotNull(message = "Username is required")
    @Length(min = 3, max = 10, message = "Username must be between 3 and 10 characters")
    private String username;

    @NotNull(message = "First name is required")
    @Length(min = 3, max = 10, message = "First name must be between 3 and 10 characters")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Length(min = 3, max = 10, message = "Last name must be between 3 and 10 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotNull(message = "Phone number is required")
    @Length(min = 10, max = 10, message = "Phone number must be 10 characters")
    private String phoneNumber;
}
