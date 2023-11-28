package softuni.exam.drive.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import softuni.exam.drive.model.enums.FuelType;

/**
 * Binding model used for data transfer for engine creation
 * @author Vasil Mirchev
 */
@Data
public class EngineBindingModel {
        @NotNull(message = "Brand is required")
        private Long brandId;

        @NotNull(message = "Fuel type is required")
        private FuelType fuelType;

        @NotNull(message = "Name is required")
        @Length(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
        private String name;

        @NotNull(message = "Horsepower is required")
        @Min(value = 40, message = "Horsepower cannot be lower than 40")
        @Max(value = 2000, message = "Horsepower cannot be more than 2000")
        private Integer horsepower;

        @NotNull(message = "Displacement is required")
        @Min(value = 800, message = "Displacement cannot be lower than 800")
        @Max(value = 8000, message = "Displacement cannot be more than 8000")
        private Integer displacement;
}
