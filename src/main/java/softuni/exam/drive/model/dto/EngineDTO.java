package softuni.exam.drive.model.dto;

import lombok.Data;
import softuni.exam.drive.model.enums.FuelType;

/**
 * Data transfer object used for engine retrieval
 * @author Vasil Mirchev
 */
@Data
public class EngineDTO {
    private Long id;
    private String name;
    private int horsepower;
    private int displacement;
    private FuelType fuelType;
}
