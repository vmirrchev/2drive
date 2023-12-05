package softuni.exam.drive.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import softuni.exam.drive.model.enums.BodyType;
import softuni.exam.drive.model.enums.DriveType;
import softuni.exam.drive.model.enums.TransmissionType;

/**
 * Entity class representing the car offer database model
 * @author Vasil Mirchev
 */
@Data
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 30)
    @NotNull
    private String title;

    @Min(1)
    @Max(1_000_000)
    @NotNull
    private int price;

    @Min(0)
    @Max(1_000_000)
    @NotNull
    private int odometer;

    @Min(1990)
    @NotNull
    private int year;

    @Length(min = 3, max = 10)
    @NotNull
    private String color;

    private byte[] picture;

    @NotNull
    private String description;

    @NotNull
    private boolean hasServiceBook;

    @NotNull
    private boolean hasAccidentDamage;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private BodyType bodyType;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TransmissionType transmissionType;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private DriveType driveType;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User addedBy;

    @ManyToOne()
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;

    @ManyToOne()
    @JoinColumn(name = "engine_id", referencedColumnName = "id")
    private Engine engine;
}
