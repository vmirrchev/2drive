package softuni.exam.drive.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import softuni.exam.drive.model.enums.FuelType;

/**
 * Entity class representing the car model database model
 * @author Vasil Mirchev
 */
@Data
@Entity
@Table(name = "engines")
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 15)
    @Column(unique = true, nullable = false)
    private String name;

    @Min(40)
    @Max(2000)
    @NotNull
    private int horsepower;

    @Min(800)
    @Max(8000)
    @NotNull
    private int displacement;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;
}
