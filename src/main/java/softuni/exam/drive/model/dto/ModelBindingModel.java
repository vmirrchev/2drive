package softuni.exam.drive.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;

import java.util.List;

/**
 * Binding model used for data transfer for car model creation
 * @author Vasil Mirchev
 */
@Data
public class ModelBindingModel {
    @NotNull(message = "Brand is required")
    private Long brandId;

    @NotNull(message = "Engine is required")
    private List<Long> engineIds;

    @NotNull(message = "Body type is required")
    private List<BodyType> bodyTypes;

    @NotNull(message = "Drive type is required")
    private List<DriveType> driveTypes;

    @NotNull(message = "Transmission type is required")
    private List<TransmissionType> transmissionTypes;

    @NotNull(message = "Name is required")
    @Length(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    private String name;

    @NotNull(message = "Start year is required")
    @Min(value = 1990, message = "Start year cannot be lower than 1990")
    private Integer startYear;

    @NotNull(message = "End year is required")
    @Min(value = 1991, message = "End year cannot be lower than 1991")
    private Integer endYear;
}
