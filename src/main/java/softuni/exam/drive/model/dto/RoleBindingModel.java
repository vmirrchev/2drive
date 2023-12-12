package softuni.exam.drive.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import softuni.exam.drive.model.enums.Role;

/**
 * Binding model used for data transfer for user role update
 * @author Vasil Mirchev
 */
@Data
public class RoleBindingModel {

    @NotNull(message = "User role is required")
    private Role role;
}
