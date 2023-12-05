package softuni.exam.drive.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;

import java.util.List;
import java.util.Set;

/**
 * Entity class representing the car model database model
 * @author Vasil Mirchev
 */
@Data
@Entity
@Table(name = "models", uniqueConstraints =
        {
                @UniqueConstraint(columnNames = {"name", "startYear"}),
                @UniqueConstraint(columnNames = {"name", "endYear"})
        })
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 20)
    @NotNull
    private String name;

    @Min(1990)
    @NotNull
    private int startYear;

    @Min(1991)
    @NotNull
    private int endYear;

    @NotNull
    @ElementCollection
    private List<BodyType> bodyTypes;

    @NotNull
    @ElementCollection
    private List<DriveType> driveTypes;

    @NotNull
    @ElementCollection
    private List<TransmissionType> transmissionTypes;

    @ManyToOne()
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToMany()
    @JoinTable(name = "model_engine",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "engine_id")
    )
    private Set<Engine> engines;
}
