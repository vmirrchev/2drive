package softuni.exam.drive.model.dto;

import lombok.Data;
import softuni.exam.drive.model.entity.Engine;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;

import java.util.List;
import java.util.Set;

/**
 * Data transfer object used for model retrieval
 * @author Vasil Mirchev
 */
@Data
public class ModelDTO {
    private Long id;
    private String name;
    private int startYear;
    private int endYear;
    private List<BodyType> bodyTypes;
    private List<DriveType> driveTypes;
    private List<TransmissionType> transmissionTypes;
    private Set<Engine> engines;
}
