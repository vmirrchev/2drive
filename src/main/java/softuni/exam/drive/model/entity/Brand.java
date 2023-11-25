package softuni.exam.drive.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Entity class representing the brand database model
 * @author Vasil Mirchev
 */
@Data
@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 15)
    @Column(unique = true, nullable = false)
    private String name;
}
