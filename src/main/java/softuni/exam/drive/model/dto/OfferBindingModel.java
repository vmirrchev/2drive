package softuni.exam.drive.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;

/**
 * Binding model used for data transfer for offer creation
 * @author Vasil Mirchev
 */
@Data
public class OfferBindingModel {

 @NotNull(message = "Model is required")
    private Long modelId;

    @NotNull(message = "Engine is required")
    private Long engineId;

    @NotNull(message = "Body type is required")
    private BodyType bodyType;

    @NotNull(message = "Drive type is required")
    private DriveType driveType;

    @NotNull(message = "Transmission type is required")
    private TransmissionType transmissionType;

    @Length(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
    @NotNull(message = "Title is required")
    private String title;

    @Min(value = 1, message = "Price cannot be lower than 1")
    @Max(value = 1_000_000, message = "Price cannot be more than 1 000 000")
    @NotNull(message = "Price is required")
    private Integer price;

    @Min(value = 0, message = "Odometer cannot be lower than 0")
    @Max(value = 1_000_000, message = "Odometer cannot be more than 1 000 000")
    @NotNull(message = "Odometer is required")
    private Integer odometer;

    @Min(value = 1990, message = "Year cannot be lower than 1990")
    @NotNull(message = "Year is required")
    private Integer year;

    @Length(min = 3, max = 10, message = "Color must be between 3 and 10 characters")
    @NotNull(message = "Color is required")
    private String color;

    @NotNull(message = "Description is required")
    private String description;

    private boolean hasServiceBook;

    private boolean hasAccidentDamage;

    private MultipartFile picture;
}
